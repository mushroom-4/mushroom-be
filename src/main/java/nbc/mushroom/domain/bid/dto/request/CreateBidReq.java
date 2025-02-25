package nbc.mushroom.domain.bid.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateBidReq(
    @Min(1_000)
    @NotNull(message = "biddingPrice는 필수 값 입니다. ")
    Long biddingPrice
) {

}
