package nbc.mushroom.domain.bid.dto.response;

import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record BidRes(
    Long bidId,
    Long biddingPrice,
    BiddingStatus biddingStatus,
    AuctionItemRes auctionItem
) {

    public static BidRes from(Bid bid) {
        return new BidRes(
            bid.getId(),
            bid.getBiddingPrice(),
            bid.getBiddingStatus(),
            new BidRes.AuctionItemRes(
                bid.getAuctionItem().getName(),
                bid.getAuctionItem().getImageUrl()
            )
        );
    }

    private record AuctionItemRes(
        String name,
        String imageUrl
    ) {

    }
}
