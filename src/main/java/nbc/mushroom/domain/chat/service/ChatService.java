package nbc.mushroom.domain.chat.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.request.ChatMessageReq;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.entity.ChatMessage;
import nbc.mushroom.domain.chat.entity.MessageType;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final String REDIS_CHAT_ROOM_KEY = "chatroom: ";
    private final RedisPublish redisPublish;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 클라이언트가 보낸 메시지를 Redis에 저장하고 전송
     *
     * @param chatRoomId     : 메시지 전송될 채팅방 ID
     * @param chatMessageReq : 클라이언트가 보낸 메시지 요청 Dto
     * @param loginUser      : 현재 로그인한 유저 (메시지 보낸 사람)
     */
    public ChatMessageRes sendChatMessage(Long chatRoomId, ChatMessageReq chatMessageReq,
        User loginUser) {

        ChatMessage chatMessage = ChatMessage.builder()
            .chatRoomId(chatRoomId)
            .messageType(MessageType.MESSAGE)
            .message(chatMessageReq.message())
            .sendDateTime(LocalDateTime.now())
            .sender(loginUser)
            .build();

        log.info("ChatMessage 객체 생성 [ChatRoomId : {}] [SenderId : {}]", chatRoomId, chatMessage);

        ChatMessageRes chatMessageRes = ChatMessageRes.from(chatMessage);
        saveChatMessage(chatRoomId, chatMessageRes);

        redisPublish.publish(chatMessageRes);

        return chatMessageRes;
    }

    public void sendBidAnnouncementMessage(Long chatRoomId, User bidder,
        Long biddingPrice) {
        String message = biddingPrice + "원에 입찰하였습니다. ";
        ChatMessage announcementMessage = ChatMessage.builder()
            .chatRoomId(chatRoomId)
            .messageType(MessageType.ANNOUNCEMENT)
            .message(message)
            .sender(bidder)
            .sendDateTime(LocalDateTime.now())
            .build();

        log.info("AnnouncementMessage 객체 생성 [ChatRoomId : {}] [SenderId : {}]", chatRoomId,
            announcementMessage);

        ChatMessageRes announcementMessageRes = ChatMessageRes.from(announcementMessage);
        saveChatMessage(chatRoomId, announcementMessageRes);

        redisPublish.publish(announcementMessageRes);
    }

    /**
     * Redis에 채팅 저장
     *
     * @param chatRoomId     채팅방 ID
     * @param chatMessageRes 저장할 메시지 객체
     */
    public void saveChatMessage(Long chatRoomId, ChatMessageRes chatMessageRes) {
        String key = REDIS_CHAT_ROOM_KEY + chatRoomId;
        log.info("Redis Storage [Key : {}]", key);
        redisTemplate.opsForList().rightPush(key, chatMessageRes); // key - value
    }

    /**
     * 특정 채팅방의 최근 메시지들 조회
     *
     * @param chatRoomId 채팅방 ID
     * @return 채팅 메시지 리스트
     */
    public List<ChatMessageRes> getChatHistory(Long chatRoomId) {
        String key = REDIS_CHAT_ROOM_KEY + chatRoomId;
        List<Object> chatMessageList = redisTemplate.opsForList().range(key, 0, -1); // 처음부터 끝까지

        // chatMessageList가 null이면 빈 리스트 반환
        if (chatMessageList == null) {
            return Collections.emptyList();
        }

        return chatMessageList.stream()
            .map(o -> (ChatMessageRes) o) // Object → ChatMessage 변환
            .toList(); // 최종 List<ChatMessageRes> 반환
    }

    /**
     * 에러 메시지가 존재하는지 확인
     */
    private boolean hasErrorMessage(StompHeaderAccessor stompHeaderAccessor) {
        String error = (String) Objects.requireNonNull(stompHeaderAccessor.getSessionAttributes())
            .get("error");
        return error != null;
    }
}
