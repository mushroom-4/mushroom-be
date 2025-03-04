package nbc.mushroom.domain.chat.service;

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
