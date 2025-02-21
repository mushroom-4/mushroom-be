package nbc.mushroom.domain.chat.event;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.service.ChatService;
import nbc.mushroom.domain.common.util.StompDestinationUtils;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatEventListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(
            event.getMessage()); // 헤더 정보 쉽게 추출할 수 있도록 감싸기
        Long chatRoomId = StompDestinationUtils.getChatRoomId(stompHeaderAccessor, "/ws/sub");

        List<ChatMessageRes> chatHistory = chatService
            .getChatHistory(chatRoomId, 0, -1); // 지금까지의 채팅 내역 가져옴
        messagingTemplate.convertAndSend(
            Objects.requireNonNull(stompHeaderAccessor.getDestination()),
            chatHistory); // 클라이언트에게 전달

        log.info("✅ [ChatRoomId : {}] 이전 채팅 내역 {}개 전송 완료", chatRoomId, chatHistory.size());
    }
}
