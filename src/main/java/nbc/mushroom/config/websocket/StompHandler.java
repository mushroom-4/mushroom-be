package nbc.mushroom.config.websocket;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUTH_TOKEN_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.BIDDING_REQUIRED;
import static nbc.mushroom.domain.common.exception.ExceptionType.CHAT_ROOM_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.EXPIRED_JWT_TOKEN;
import static nbc.mushroom.domain.common.exception.ExceptionType.INTERNAL_SERVER_ERROR;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_JWT;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_JWT_SIGNATURE;
import static nbc.mushroom.domain.common.exception.ExceptionType.UNSUPPORTED_JWT_TOKEN;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.service.AuctionItemService;
import nbc.mushroom.domain.bid.service.BidService;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.JwtUtil;
import nbc.mushroom.domain.common.util.StompDestinationUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

// 메시지 전송을 가로채서 권한 검증
@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final BidService bidService;
    private final AuctionItemService auctionItemService;
    private final JwtUtil jwtUtil;

    /**
     * STOMP 메시지가 전송되기 전에 호출
     *
     * STOMP 명령어에 따라 필요한 작업 처리
     * - SUBSCRIBE: 클라이언트가 특정 토픽을 구독할 때 처리.
     * - SEND: 클라이언트가 메시지를 보낼 때 처리.
     *
     * @param message : 클라이언트가 전송한 메시지
     * @param channel : 메시지가 전달될 채널
     * @return message : 메시지 수정 작업 없어서 들어온거 그대로 반환함
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = stompHeaderAccessor.getCommand();

        if (command == null) {
            return message;
        }

        switch (command) {
            case CONNECT:
                handleConnect(stompHeaderAccessor);
                break;
            case SUBSCRIBE:
                handleSubscribe(stompHeaderAccessor);
                break;
            case SEND:
                handleSend(stompHeaderAccessor);
                break;
            default:
                break;
        }
        return message;
    }

    /**
     * STOMP CONNECT 요청 시, JWT 토큰 검증 및 사용자 정보 저장
     */
    private void handleConnect(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: CONNECT 요청 감지 ::::");

        String bearerJwt = getAuthorizationHeader(stompHeaderAccessor);

        log.info("✅ 받은 토큰: {}", bearerJwt);

        if (bearerJwt == null) {
            throw new CustomException(AUTH_TOKEN_NOT_FOUND);
        }

        String jwt = bearerJwt.substring(7); // "Bearer " 제거

        try {
            Claims claims = jwtUtil.extractClaims(jwt);

            if (claims == null) {
                throw new CustomException(INVALID_JWT);
            }

            log.info("✅ userId : {}", claims.getSubject());

            // STOMP 세션에 사용자 정보 저장 (나중에 사용 가능)
            stompHeaderAccessor.getSessionAttributes()
                .put("userId", Long.parseLong(claims.getSubject()));
            stompHeaderAccessor.getSessionAttributes().put("email", claims.get("email"));
            stompHeaderAccessor.getSessionAttributes().put("nickname", claims.get("nickname"));
            Optional.ofNullable(claims.get("imageUrl"))
                .ifPresent(url -> stompHeaderAccessor.getSessionAttributes().put("imageUrl", url));
            stompHeaderAccessor.getSessionAttributes().put("userRole", claims.get("userRole"));

            log.info("✅ CONNECT 성공, STOMP 세션 저장 확인: {}",
                stompHeaderAccessor.getSessionAttributes());
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            throw new CustomException(INVALID_JWT_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            throw new CustomException(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            throw new CustomException(UNSUPPORTED_JWT_TOKEN);
        } catch (Exception e) {
            log.error("Internal server error", e);
            throw new CustomException(INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 채팅방 구독 요청 시
     * 해당 경매 물품이 존재하는지 확인 ( chatRoomId == auctionItemId )
     */
    private void handleSubscribe(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: SUBSCRIBE 요청 감지 ::::");
        try {
            Long userId = (Long) stompHeaderAccessor.getSessionAttributes().get("userId");

            if (userId == null) {
                throw new CustomException(AUTH_TOKEN_NOT_FOUND);
            }

            log.info("✅ SUBSCRIBE 요청 destination: {}", stompHeaderAccessor.getDestination());

            Long chatRoomId = StompDestinationUtils.getChatRoomId(stompHeaderAccessor, "/ws/sub");
            if (Boolean.FALSE.equals(auctionItemService.hasAuctionItem(chatRoomId))) {
                throw new CustomException(CHAT_ROOM_NOT_FOUND);
            }

            log.info("✅ SUBSCRIBE 성공: userId={}, chatRoomId={}", userId, chatRoomId);
        } catch (Exception e) {
            log.error("❌ SUBSCRIBE 실패: {}", e.getMessage());
            throw e; // 예외를 던져야 STOMP에서 처리 가능
        }
    }

    /**
     * 메시지 전송 시
     * 해당 경매에 입찰한 기록이 있는 유저인지 확인
     */
    private void handleSend(StompHeaderAccessor stompHeaderAccessor) {
        log.info(":::: SEND 요청 감지 ::::");
        try {
            Long chatRoomId = StompDestinationUtils.getChatRoomId(stompHeaderAccessor, "/ws/pub");
            Long loginUserId = (Long) stompHeaderAccessor.getSessionAttributes().get("userId");

            if (loginUserId == null) {
                throw new CustomException(AUTH_TOKEN_NOT_FOUND);
            }

            if (Boolean.FALSE.equals(bidService.hasBid(loginUserId, chatRoomId))) {
                throw new CustomException(BIDDING_REQUIRED);
            }

            log.info("✅ SEND 성공: userId={}, chatRoomId={}", loginUserId, chatRoomId);
        } catch (Exception e) {
            log.error("❌ SEND 실패: {}", e.getMessage());
            throw e; // 예외를 던져야 STOMP에서 처리 가능
        }
    }

    /**
     * STOMP 헤더에서 `Authorization` 값을 가져옴
     */
    private String getAuthorizationHeader(StompHeaderAccessor accessor) {
        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        return (authHeaders != null && !authHeaders.isEmpty()) ? authHeaders.get(0) : null;
    }
}