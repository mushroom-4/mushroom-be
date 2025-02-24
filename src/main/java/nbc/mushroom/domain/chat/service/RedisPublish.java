package nbc.mushroom.domain.chat.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.chat.entity.ChatMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublish {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic; // 메시지 발행할 Reids 채널

    /**
     * 클라이언트가 보낸 메시지를 Redis 채널에 전송
     *
     * ChatMessageRes 객체를 Redis 채널에 전송
     * 해당 채널의 모든 구독자(클라이언트)에게 메시지를 브로드캐스트
     *
     * @param chatMessage : 메시지 정보
     */
    public void publish(ChatMessage chatMessage) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }
}
