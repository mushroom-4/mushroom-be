package nbc.mushroom.domain.bid.dto.response;

import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record CreateBidRes(
    Long biddingPrice,
    BiddingStatus biddingStatus
) {

}
