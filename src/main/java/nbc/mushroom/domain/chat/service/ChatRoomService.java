package nbc.mushroom.domain.chat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

        List<String> userSessionList = userSessionObj != null
            // 있으면 ,로 구분해서 리스트로 바꿔
            ? new ArrayList<>(Arrays.asList(userSessionObj.toString().split(",")))
            : new ArrayList<>();

        if (!userSessionList.contains(sessionId)) { // List에 없으면 추가해
            userSessionList.add(sessionId);
            log.info(
                "[채팅방 접속] [chatRoomId={}] [userId={}] [새로운 세션 sessionId={}] [Redis Storage Key={}]",
                chatRoomId, userId, sessionId, key);
        }

        redisTemplate.opsForHash()
            .put(key, userId, String.join(",", userSessionList));
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
}
