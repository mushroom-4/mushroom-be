package nbc.mushroom.domain.chat.dto.request;

import jakarta.validation.constraints.NotNull;

public record ChatMessageReq(

    @NotNull(message = "메시지를 입력해주세요")
    String message
) {

}
