package nbc.mushroom.domain.auth.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.AUTH_FAILED;
import static nbc.mushroom.domain.common.exception.ExceptionType.EMAIL_DUPLICATE;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auth.dto.request.UserLoginReq;
import nbc.mushroom.domain.auth.dto.request.UserRegisterReq;
import nbc.mushroom.domain.auth.dto.response.TokenRes;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.JwtUtil;
import nbc.mushroom.domain.common.util.PasswordEncoder;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.entity.UserRole;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ImageUtil imageUtil;

    @Transactional
    public TokenRes register(UserRegisterReq userRegisterReq) {
        if (userRepository.existsByEmail(userRegisterReq.email())) {
            throw new CustomException(EMAIL_DUPLICATE);
        }

        UserRole userRole = UserRole.of(userRegisterReq.userRole());
        String encodedPassword = passwordEncoder.encode(userRegisterReq.password());

        String fileName = imageUtil.upload(userRegisterReq.image());

        User savedUser = userRepository.save(
            User.builder()
                .email(userRegisterReq.email())
                .password(encodedPassword)
                .userRole(userRole)
                .nickname(userRegisterReq.nickname())
                .imageUrl(fileName)
                .build()
        );

        String bearerToken = jwtUtil.createToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getNickname(),
            imageUtil.getImageUrl(savedUser.getImageUrl()),
            userRole
        );

        return new TokenRes(bearerToken);
    }

    public TokenRes login(UserLoginReq userLoginReq) {
        User user = userRepository.findByEmail(userLoginReq.email())
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(userLoginReq.password(), user.getPassword())) {
            throw new CustomException(AUTH_FAILED);
        }

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
