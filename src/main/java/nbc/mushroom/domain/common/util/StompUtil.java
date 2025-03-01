package nbc.mushroom.domain.common.util;

import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_CHAT_ROOM_PATH;

import nbc.mushroom.domain.common.exception.CustomException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class StompUtil {

    /**
     * STOMP 메시지의 Destination에서 채팅방 ID 추출 후 검증
     */
    public static Long getChatRoomId(StompHeaderAccessor stompHeaderAccessor, String prefix) {
        String destination = stompHeaderAccessor.getDestination();
        if (destination == null || !destination.startsWith(prefix + "/chats/")) {
            throw new CustomException(INVALID_CHAT_ROOM_PATH);
        }

        String chatRoomIdStr = destination.substring((prefix + "/chats/").length());
        if (chatRoomIdStr.isEmpty() || !chatRoomIdStr.matches("\\d+")) {
            throw new CustomException(INVALID_CHAT_ROOM_PATH);
        }

        return Long.parseLong(chatRoomIdStr);
    }
}
