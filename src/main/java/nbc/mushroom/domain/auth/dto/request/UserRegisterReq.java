package nbc.mushroom.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserRegisterReq(
    @NotBlank
    @Email
    String email,

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,20}$",
        message = "새 비밀번호는 8자 이상 20자 이하여야 하고, 숫자와 대문자를 포함해야 합니다."
    )
    String password,

    @NotBlank
    @Pattern(
        regexp = "^(?i)(admin|user)$", // (?i) 대소문자 구분 없이 허용
        message = "userRole은 'admin' 또는 'user'만 가능합니다."
    )
    String userRole,

    @NotBlank
    @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
    String nickname,

    MultipartFile image
) {

}
