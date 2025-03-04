package nbc.mushroom.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserReq(
    @NotBlank
    @Email
    String email,

    @NotBlank
    String password
) {

}
