package nbc.mushroom.domain.chat.event;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.constant.RedisChatRoomKey;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatRoomEventListener {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 서버 종료가 감지되면 발생하는 이벤트
     *
     * 접속자 목록을 삭제함
     */
    @EventListener
    public void cleanupConcurrentUsersOnShutdown(ContextClosedEvent event) {
        log.info("애플리케이션 종료 감지 - 채팅방 세션 정리 시작...");

        String key = RedisChatRoomKey.getConcurrenUserStorageKey("*");

        // 특정 패턴을 가진 Redis 키 삭제 (예: chatroom::?::users)
        Set<String> chatroomKeys = redisTemplate.keys(key);
        if (chatroomKeys != null && !chatroomKeys.isEmpty()) {
            redisTemplate.delete(chatroomKeys);
        }
        log.info("채팅방 세션 정리 완료 - {}개의 채팅방 삭제", chatroomKeys.size());
    }
}
