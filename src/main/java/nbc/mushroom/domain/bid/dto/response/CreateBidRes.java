package nbc.mushroom.domain.bid.dto.response;

import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record CreateBidRes(
    Long biddingPrice,
    BiddingStatus biddingStatus
) {

    public static CreateBidRes from(Bid bid) {
        return new CreateBidRes(
            bid.getBiddingPrice(),
            bid.getBiddingStatus()
        );
    }
}
