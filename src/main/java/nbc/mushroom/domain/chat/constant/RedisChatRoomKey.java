package nbc.mushroom.domain.chat.constant;

public class RedisChatRoomKey {

    public static final String REDIS_CHAT_ROOM_KEY = "chatroom::";
    public static final String REDIS_CHAT_ROOM_CONCURRENT_USERS_KEY = "::users";
    private static final String REDIS_CHAT_ROOM_MESSAGE_KEY = "::message";

    /**
     * 동시 접속자 저장소 키
     */
    public static String getConcurrenUserStorageKey(String chatRoomId) {
        return REDIS_CHAT_ROOM_KEY + chatRoomId + REDIS_CHAT_ROOM_CONCURRENT_USERS_KEY;
    }

    /**
     * 메시지 저장소 키
     */
    public static String getMessageStorageKey(Long chatRoomId) {
        return REDIS_CHAT_ROOM_KEY + chatRoomId.toString() + REDIS_CHAT_ROOM_MESSAGE_KEY;
    }
}
