package nbc.mushroom.domain.user.dto.response;

import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record UserBidRes(
    Long bidId,
    Long biddingPrice,
    BiddingStatus biddingStatus,
    SearchAuctionItemRes searchAuctionItemRes
) {

    public static UserBidRes from(Bid bid, SearchAuctionItemRes searchAuctionItemRes) {
        return new UserBidRes(
            bid.getId(),
            bid.getBiddingPrice(),
            bid.getBiddingStatus(),
            searchAuctionItemRes
        );
    }
}
