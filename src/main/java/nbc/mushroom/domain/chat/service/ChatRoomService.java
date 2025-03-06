package nbc.mushroom.domain.chat.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.chat.constant.RedisChatRoomKey;
import nbc.mushroom.domain.chat.dto.response.ConcurrentUserListRes;
import nbc.mushroom.domain.chat.dto.response.ConcurrentUserListRes.UserInfoRes;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final RedisPublish redisPublish;

    /**
     * 채팅방에 접속하면 유저Id와 세션Id 저장 Redis Hash에 저장
     */
    public void addSessionId(String chatRoomId, String userId, String sessionId) {
        String key = RedisChatRoomKey.getConcurrenUserStorageKey(chatRoomId);

        Object userSessionObj = redisTemplate.opsForHash()
            .get(key, userId);

        Set<String> userSessionSet = userSessionObj != null
            ? new HashSet<>(Arrays.asList(userSessionObj.toString().split(","))) // 기존 데이터를 Set으로 변환
            : new HashSet<>(); // 없으면 새로운 Set 생성

        // Set에 세션 id 추가
        userSessionSet.add(sessionId);

        log.info(
            "[채팅방 접속] [chatRoomId={}] [userId={}] [새로운 세션 sessionId={}] [Redis Storage Key={}]",
            chatRoomId, userId, sessionId, key
        );

        redisTemplate.opsForHash().put(key, userId, String.join(",", userSessionSet));

        sendConcurrentUserList(Long.parseLong(chatRoomId));
    }

    /**
     * 채팅방을 나가면 세션Id 저장 Redis Hash에서 제거
     */
    public void removeSessionId(String chatRoomId, String userId, String sessionId) {
        String key = RedisChatRoomKey.getConcurrenUserStorageKey(chatRoomId);

        Object existingSessionsObj = redisTemplate.opsForHash().get(key, userId);
        if (existingSessionsObj == null) {
            log.warn("[채팅방 퇴장 실패] [chatRoomId={}] [userId={}] [sessionId={}] - 존재하지 않는 세션",
                chatRoomId,
                userId, sessionId);
            return; // 등록된 세션이 없으면 종료
        }

        Set<String> userSessionList = new HashSet<>(
            Arrays.asList(existingSessionsObj.toString().split(",")));

        userSessionList.remove(sessionId);

        if (userSessionList.isEmpty()) {
            // 세션이 없으면 userId 키 자체를 삭제
            redisTemplate.opsForHash().delete(key, userId);
            log.info("[채팅방 퇴장] [chatRoomId={}] [userId={}] - 모든 세션 종료",
                chatRoomId, userId);
        } else {
            // 삭제할 세션 삭제한 리스트로 다시 저장
            redisTemplate.opsForHash().put(key, userId, String.join(",", userSessionList));
            log.info("[채팅방 퇴장] [chatRoomId={}] [userId={}], [남은 세션 수={}] [Redis Storage Key={}]",
                chatRoomId, userId, userSessionList.size(), key);
        }

        sendConcurrentUserList(Long.parseLong(chatRoomId));
    }

    // 현재 접속자 목록 전송
    public void sendConcurrentUserList(Long chatRoomId) {
        List<UserInfoRes> userInfoResList = getConcurrentUsers(chatRoomId.toString());
        ConcurrentUserListRes concurrentUserListRes = ConcurrentUserListRes.from(chatRoomId,
            userInfoResList);

        redisPublish.publishConcurrentUserList(concurrentUserListRes);
    }

    // Redis에서 현재 접속자 목록을 가져옴
    private List<UserInfoRes> getConcurrentUsers(String chatRoomId) {
        Set<Long> userIdSet = redisTemplate.opsForHash()
            .keys(RedisChatRoomKey.getConcurrenUserStorageKey(chatRoomId))
            .stream()
            .map(key -> Long.valueOf(key.toString()))
            .collect(Collectors.toSet());

        if (userIdSet == null || userIdSet.isEmpty()) {
            log.info("[채팅방 접속자 조회] chatRoomId={} - 현재 접속자 없음", chatRoomId);
            return Collections.emptyList();
        }

        List<User> userList = userRepository.findAllById(userIdSet);
        log.info("[채팅방 접속자 조회] chatRoomId={} - 접속자 수={}", chatRoomId, userList.size());

        return userList.stream()
            .map(UserInfoRes::from)
            .toList();
    }

    /**
     * 채팅방 메시지 저장소 만료 시간 설정
     */
    public void setChatRoomStorageTTL(Long chatRoomId) {
        String key = RedisChatRoomKey.getMessageStorageKey(chatRoomId);

        Boolean hasKey = redisTemplate.hasKey(key);
        if (!hasKey) {
            return;
        }

        Duration ttl = Duration.ofDays(7);

        redisTemplate.expire(key, ttl);

        Long expireSecond = redisTemplate.getExpire(key);
        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(expireSecond);

        log.info("[채팅방 TTL 설정] chatRoomId={}, Redis Storage Key={}, 만료 예정 시간={}",
            chatRoomId, key, expirationTime);
    }
}
