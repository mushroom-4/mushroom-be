package nbc.mushroom.config.websocket;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUTH_WRONG_USED;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.user.entity.UserRole;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompAuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthAnnotation = parameter.getParameterAnnotation(Auth.class) != null;
        boolean isAuthUserType = parameter.getParameterType().equals(AuthUser.class);

        // @Auth 어노테이션과 AuthUser 타입이 함께 사용되지 않은 경우 예외 발생
        if (hasAuthAnnotation != isAuthUserType) {
            throw new CustomException(AUTH_WRONG_USED);
        }

        return hasAuthAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

        if (sessionAttributes == null || !sessionAttributes.containsKey("userId")) {
            throw new CustomException(ExceptionType.JWT_TOKEN_REQUIRED);
        }

        // STOMP 세션에서 사용자 정보 가져오기
        Long userId = (Long) sessionAttributes.get("userId");
        String email = (String) sessionAttributes.get("email");
        String nickname = (String) sessionAttributes.get("nickname");
        String imageUrl = (String) sessionAttributes.get("imageUrl");
        UserRole userRole = UserRole.of((String) sessionAttributes.get("userRole"));

        return new AuthUser(userId, email, nickname, imageUrl, userRole);
    }
}
