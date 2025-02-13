package nbc.mushroom.domain.user.dto.response;

import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record GetUserBidRes(
    Long bidId,
    Long biddingPrice,
    BiddingStatus biddingStatus,
    SearchAuctionItemRes searchAuctionItemRes
) {

    public static GetUserBidRes from(Bid bid, SearchAuctionItemRes searchAuctionItemRes) {
        return new GetUserBidRes(
            bid.getId(),
            bid.getBiddingPrice(),
            bid.getBiddingStatus(),
            searchAuctionItemRes
        );
    }
}
