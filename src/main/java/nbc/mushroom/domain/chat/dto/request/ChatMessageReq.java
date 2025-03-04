package nbc.mushroom.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChatMessageReq(

    @Size(max = 50)
    @NotNull(message = "메시지를 입력해주세요")
    String message
) {

}
