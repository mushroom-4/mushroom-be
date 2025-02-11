package nbc.mushroom.domain.user.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.PASSWORD_NOT_MATCH;
import static nbc.mushroom.domain.common.exception.ExceptionType.PASSWORD_SAME;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.PasswordEncoder;
import nbc.mushroom.domain.user.dto.request.UserPasswordChangeReq;
import nbc.mushroom.domain.user.dto.response.UserRes;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRes getUser(long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return UserRes.toDto(user);
    }

    @Transactional
    public void changePassword(long userId, UserPasswordChangeReq userPasswordChangeReq) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (passwordEncoder.matches(userPasswordChangeReq.newPassword(),
            user.getPassword())) {
            throw new CustomException(PASSWORD_SAME);
        }

        if (!passwordEncoder.matches(userPasswordChangeReq.oldPassword(),
            user.getPassword())) {
            throw new CustomException(PASSWORD_NOT_MATCH);
        }

        user.changePassword(passwordEncoder.encode(userPasswordChangeReq.newPassword()));
    }
}
