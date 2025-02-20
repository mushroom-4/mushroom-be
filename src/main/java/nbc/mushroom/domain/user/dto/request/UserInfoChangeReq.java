package nbc.mushroom.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserInfoChangeReq(
    @NotBlank
    @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
    String nickname,

    MultipartFile image
) {

}
