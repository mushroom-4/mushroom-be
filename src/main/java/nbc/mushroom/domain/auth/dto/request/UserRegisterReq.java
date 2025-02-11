package nbc.mushroom.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

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
    String userRole,

    @NotBlank
    String nickname
) {

}
