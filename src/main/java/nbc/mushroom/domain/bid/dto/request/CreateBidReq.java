package nbc.mushroom.domain.bid.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateBidReq(
    @NotNull(message = "biddingPrice는 필수 값 입니다. ")
    Long biddingPrice
) {

}
