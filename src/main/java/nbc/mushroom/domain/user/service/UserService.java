package nbc.mushroom.domain.user.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.PASSWORD_NOT_MATCH;
import static nbc.mushroom.domain.common.exception.ExceptionType.PASSWORD_SAME;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auth.dto.response.TokenRes;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.JwtUtil;
import nbc.mushroom.domain.common.util.PasswordEncoder;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.user.dto.request.UpdateUserInfoReq;
import nbc.mushroom.domain.user.dto.request.UpdateUserPasswordReq;
import nbc.mushroom.domain.user.dto.response.UserRes;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageUtil imageUtil;
    private final JwtUtil jwtUtil;

    public UserRes getUser(long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        return UserRes.from(user);
    }

    @Transactional
    public void updatePassword(long userId, UpdateUserPasswordReq updateUserPasswordReq) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (passwordEncoder.matches(updateUserPasswordReq.newPassword(),
            user.getPassword())) {
            throw new CustomException(PASSWORD_SAME);
        }

        if (!passwordEncoder.matches(updateUserPasswordReq.oldPassword(),
            user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        user.changePassword(passwordEncoder.encode(updateUserPasswordReq.newPassword()));
    }

    @Transactional
    public TokenRes updateUserInfo(AuthUser authUser, UpdateUserInfoReq updateUserInfoReq) {
        User user = userRepository.findById(authUser.id())
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        log.info("dto: {}", updateUserInfoReq);
        String fileName = imageUtil.upload(updateUserInfoReq.image());

        if (fileName != null) { // 새로 등록한 파일이 있다면, 이전 파일 삭제
            imageUtil.delete(user.getImageUrl());
        }
        user.updateInfo(updateUserInfoReq.nickname(), fileName);

        String bearerToken = jwtUtil.createToken(
            user.getId(),
            user.getEmail(),
            user.getNickname(),
            imageUtil.getImageUrl(user.getImageUrl()),
            user.getUserRole()
        );

        return new TokenRes(bearerToken);
    }
}
