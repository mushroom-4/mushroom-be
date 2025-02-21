package nbc.mushroom.domain.chat.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.entity.ChatMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate; // Redis에서 수신한 메시지의 역직렬화를 하기 위해
    private final SimpMessagingTemplate simpMessagingTemplate; // STOMP 메시지 전송을 위한 템플릿. WebSocket 연결된 클라이언트에게 메시지 보냄

    /**
     * Redis에서 받은 메시지를 WebSocket을 통해 클라이언트에게 전달
     *
     * Redis의 Pub/Sub을 통해 발행된 메시지를 수신하면 호출됨
     * 해당 메시지를 WebSocket을 통해 클라이언트에게 전달
     *
     * @param message Redis에서 수신한 메시지 (바이트 배열로 전달됨)
     * @param pattern 메시지를 발행한 Redis 채널 패턴 (구독한 채널 정보)
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        ChatMessage chatMessage = (ChatMessage) redisTemplate.getValueSerializer()
            .deserialize(message.getBody());

        ChatMessageRes chatMessageRes = ChatMessageRes.from(Objects.requireNonNull(chatMessage));

        log.info("[ChatRoomId: {}] [{}] {}: {} ( {} )",
            chatMessageRes.chatRoomId(),
            chatMessageRes.messageType(),
            chatMessageRes.nickname(),
            chatMessageRes.message(),
            chatMessageRes.sendDateTime()
        );

        // Redis에서 받은 메시지 WebSocket으로 전달 ( /ws/sub/chats/ -> 웹소켓 구독 경로 )
        simpMessagingTemplate.convertAndSend("/ws/sub/chats/" + chatMessageRes.chatRoomId(),
            chatMessageRes);
    }
}
