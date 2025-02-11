package nbc.mushroom.domain.bid.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateBidReq(
    @NotNull
    Long bidPrice
) {

}
