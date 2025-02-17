package nbc.mushroom.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginReq(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String password
) {

}
