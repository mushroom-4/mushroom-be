package nbc.mushroom.domain.common.util;

import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_CHAT_ROOM_PATH;
import static nbc.mushroom.domain.common.exception.ExceptionType.JWT_TOKEN_REQUIRED;

import java.util.List;
import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.AntPathMatcher;

public class StompUtil {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    // STOMP 경로 패턴 리스트
    private static final List<String> chatRoomPatterns = List.of(
        "/ws/**/chats/{chatRoomId}/**",
        "/ws/**/chatrooms/{chatRoomId}/users"
    );

    /**
     * STOMP 메시지의 Destination에서 채팅방 ID 추출 후 검증
     */
    public static Long getChatRoomId(StompHeaderAccessor stompHeaderAccessor) {
        String destination = stompHeaderAccessor.getDestination();
        if (destination == null) {
            throw new CustomException(INVALID_CHAT_ROOM_PATH);
        }

        return chatRoomPatterns.stream()
            .filter(pattern -> pathMatcher.match(pattern, destination))
            .map(pattern -> pathMatcher.extractUriTemplateVariables(pattern, destination)
                .get("chatRoomId"))
            .map(Long::valueOf)
            .findFirst()
            .orElseThrow(() -> new CustomException(INVALID_CHAT_ROOM_PATH));
    }

    /**
     * STOMP 요청에서 사용자 ID 추출
     */
    public static Long getUserId(StompHeaderAccessor stompHeaderAccessor) {
        Long userId = (Long) stompHeaderAccessor.getSessionAttributes().get("userId");
        if (userId == null) {
            throw new CustomException(JWT_TOKEN_REQUIRED);
        }
        return userId;
    }
}
