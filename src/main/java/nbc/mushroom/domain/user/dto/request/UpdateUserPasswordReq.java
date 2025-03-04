package nbc.mushroom.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateUserPasswordReq(
    @NotBlank
    String oldPassword,

    @NotBlank
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[A-Z]).{8,20}$",
        message = "새 비밀번호는 8자 이상 20자 이하여야 하고, 숫자와 대문자를 포함해야 합니다."
    )
    String newPassword
) {

}
