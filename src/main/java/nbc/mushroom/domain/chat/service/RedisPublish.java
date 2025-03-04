package nbc.mushroom.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.dto.response.ChatMessageRes;
import nbc.mushroom.domain.chat.dto.response.ConcurrentUserListRes;
import nbc.mushroom.domain.chat.entity.RedisSubMessage;
import nbc.mushroom.domain.chat.entity.SubMessageType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
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
     * @param chatMessageRes : 메시지 정보
     */
    public void publish(ChatMessageRes chatMessageRes) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageRes);
    /**
     * 현재 채팅방 접속자 목록 Redis 채널에 전송
     */
    public void publishConcurrentUserList(ConcurrentUserListRes concurrentUserListRes) {
        RedisSubMessage redisSubMessage = new RedisSubMessage(SubMessageType.CONCURRENT_USER_LIST,
            concurrentUserListRes);
        redisTemplate.convertAndSend(channelTopic.getTopic(), redisSubMessage);
        log.info("[Redis Publish] 채팅방 접속자 목록 발행 - chatRoomId={}, 접속자 수={}",
            concurrentUserListRes.chatRoomId(), concurrentUserListRes.concurrentUserCount());
    }
}
