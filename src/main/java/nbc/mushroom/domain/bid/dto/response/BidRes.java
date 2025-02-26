package nbc.mushroom.domain.bid.dto.response;

import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record BidRes(
    Long bidId,
    Long biddingPrice,
    BiddingStatus biddingStatus,
    SearchAuctionItemRes searchAuctionItemRes
) {

    public static BidRes from(Bid bid) {
        return new BidRes(
            bid.getId(),
            bid.getBiddingPrice(),
            bid.getBiddingStatus(),
            SearchAuctionItemRes.from(bid.getAuctionItem())
        );
    }
}
