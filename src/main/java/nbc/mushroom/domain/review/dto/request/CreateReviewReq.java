package nbc.mushroom.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewReq(
    @NotNull(message = "점수는 필수 값 입니다. ")
    @Min(1)
    @Max(10)
    int score,

    @NotNull(message = "내용은 필수 값 입니다. ")
    @Size(max = 100, message = "내용은 100자 이내 입니다. ")
    String content) {

}
