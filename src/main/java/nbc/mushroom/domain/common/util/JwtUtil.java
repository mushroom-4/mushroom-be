package nbc.mushroom.domain.common.util;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUTH_TOKEN_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.EXPIRED_JWT_TOKEN;
import static nbc.mushroom.domain.common.exception.ExceptionType.INTERNAL_SERVER_ERROR;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_JWT;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_JWT_SIGNATURE;
import static nbc.mushroom.domain.common.exception.ExceptionType.JWT_TOKEN_REQUIRED;
import static nbc.mushroom.domain.common.exception.ExceptionType.UNSUPPORTED_JWT_TOKEN;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.user.entity.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createToken(
        Long userId, String email, String nickname, String imageUrl, UserRole userRole
    ) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("nickname", nickname)
                .claim("userRole", userRole)
                .claim("imageUrl", imageUrl)
                .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                .compact();
    }

    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(JWT_TOKEN_REQUIRED);
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    /**
     * JJWT 토큰에서 사용자 정보를 가져옴 (HTTP 용)
     * HttpResponse 예외 처리 방식 적용
     */
    public Map<String, Object> getUserInfoFromTokenForHttp(String bearerToken,
        HttpServletResponse httpResponse) {
        return extractUserInfoFromToken(bearerToken, e -> {
            log.error("Http 연결 실패 - {}", e.getMessage(), e);
            try {
                httpResponse.sendError(e.getHttpStatus().value(), e.getMessage());
            } catch (IOException ex) {
                log.error("Http 응답 처리 중 오류 발생", ex);
            }
        });
    }

    /**
     * JWT 토큰에서 사용자 정보를 가져옴 (WebSocket용)
     * WebSocket 예외 처리 방식 적용
     */
    public Map<String, Object> getUserInfoFromTokenForWebSocket(String bearerToken) {
        return extractUserInfoFromToken(bearerToken, e -> {
            log.error("WebSocket 연결 실패 - {}", e.getMessage(), e);
            throw e;
        });
    }

    /**
     * JWT 토큰 검증 후 사용자 정보 추출
     * 사용자 정보 추출은 메서드 호출로 이루어짐
     *
     * @param exceptionHandler
     * : getUserInfoFromTokenForWebSocket에서 호출하면 CustomException으로 예외처리
     * getUserInfoFromTokenForHttp에서 호출하면 httpResponse.sendError로 예외처리
     * 상황에 따라 다르게 동작할 수 있도록 해주는 함수형 인터페이스
     */
    private Map<String, Object> extractUserInfoFromToken(String bearerToken,
        Consumer<CustomException> exceptionHandler) {
        if (bearerToken == null) {
            exceptionHandler.accept(new CustomException(JWT_TOKEN_REQUIRED));
            return Collections.emptyMap();
        }

        try {
            return parseTokenUserInfo(bearerToken);
        } catch (CustomException e) {
            exceptionHandler.accept(e);
            return Collections.emptyMap();
        }
    }

    /**
     * JWT 토큰에서 사용자 정보를 추출
     */
    private Map<String, Object> parseTokenUserInfo(String bearerToken) {
        String jwt = substringToken(bearerToken); // "Bearer " 제거

        try {
            Claims claims = extractClaims(jwt);

            if (claims == null) {
                throw new CustomException(INVALID_JWT);
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", Long.parseLong(claims.getSubject()));
            userInfo.put("email", claims.get("email"));
            userInfo.put("nickname", claims.get("nickname"));
            userInfo.put("userRole", claims.get("userRole"));
            Optional.ofNullable(claims.get("imageUrl"))
                .ifPresent(url -> userInfo.put("imageUrl", url));

            return userInfo;
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            throw new CustomException(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(UNSUPPORTED_JWT_TOKEN);
        } catch (Exception e) {
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }
}
