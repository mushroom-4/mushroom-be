package nbc.mushroom.domain.chat.service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.request.ChatMessageReq;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.entity.ChatMessage;
import nbc.mushroom.domain.chat.entity.MessageType;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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
     * hasErrorMessage가 true면 에러메시지 생성, false면 일반 메시지 생성
     *
     * @param chatRoomId     : 메시지 전송될 채팅방 ID
     * @param chatMessageReq : 클라이언트가 보낸 메시지 요청 Dto
     * @param loginUser      : 현재 로그인한 유저 (메시지 보낸 사람)
     * @param stompHeaderAccessor : STOMP 헤더 정보에 접근하기 위한 클래스 (Error메시지가 담겼는지 확인하기 위해)
     */
    public ChatMessageRes sendChatMessage(Long chatRoomId, ChatMessageReq chatMessageReq,
        StompHeaderAccessor stompHeaderAccessor,
        User loginUser) {

        boolean isError = hasErrorMessage(stompHeaderAccessor);

        ChatMessage chatMessage = isError
            ? createErrorMessage(chatRoomId, stompHeaderAccessor, loginUser)
            : createChatMessage(chatRoomId, chatMessageReq.message(), loginUser);

        ChatMessageRes chatMessageRes = ChatMessageRes.from(chatMessage);

        // 에러 메시지가 아닐 때만 저장
        if (!isError) {
            saveChatMessage(chatRoomId, chatMessageRes);
        }

        redisPublish.publish(chatMessageRes);

        return chatMessageRes;
    }

    public void sendBidAnnouncementMessage(Long chatRoomId, User bidder,
        Long biddingPrice) {

        NumberFormat numberFormat = NumberFormat.getInstance(Locale.KOREA);
        String formattedPrice = numberFormat.format(biddingPrice);

        String message = formattedPrice + "원에 입찰하였습니다.";

        ChatMessage announcementMessage = ChatMessage.builder()
            .chatRoomId(chatRoomId)
            .messageType(MessageType.ANNOUNCEMENT)
            .message(message)
            .sender(bidder)
            .sendDateTime(LocalDateTime.now())
            .build();

        log.info("AnnouncementMessage 객체 생성 [ChatRoomId : {}] [SenderId : {}]", chatRoomId,
            bidder.getId());

        ChatMessageRes announcementMessageRes = ChatMessageRes.from(announcementMessage);
        saveChatMessage(chatRoomId, announcementMessageRes);

        redisPublish.publish(announcementMessageRes);
    }

    /**
     * 일반 메시지 생성 메서드
     */
    private ChatMessage createChatMessage(Long chatRoomId, String message, User sender) {
        log.info("ChatMessage 객체 생성 [ChatRoomId : {}] [SenderId : {}]", chatRoomId,
            sender.getId());
        return ChatMessage.builder()
            .chatRoomId(chatRoomId)
            .messageType(MessageType.MESSAGE)
            .message(message)
            .sendDateTime(LocalDateTime.now())
            .sender(sender)
            .build();
    }

    /**
     * 에러 메시지 생성 메서드
     */
    private ChatMessage createErrorMessage(Long chatRoomId,
        StompHeaderAccessor stompHeaderAccessor,
        User sender) {
        ChatMessage chatErrorMessage = ChatMessage.builder()
            .chatRoomId(chatRoomId)
            .messageType(MessageType.ERROR)
            .message((String) stompHeaderAccessor.getSessionAttributes().get("error"))
            .sendDateTime(LocalDateTime.now())
            .sender(sender)
            .build();

        log.info("ErrorMessage 객체 생성 [ChatRoomId : {}] [SenderId : {}] [ErrorMessage : {}]",
            chatRoomId, sender.getId(), chatErrorMessage.getMessage());
        return chatErrorMessage;
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
