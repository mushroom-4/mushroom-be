package nbc.mushroom.domain.bid.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;

public record BidInfoRes(
    Long bidId,
    Long biddingPrice,
    BiddingStatus biddingStatus,
    AuctionItemRes auctionItem,
    ReviewRes review
) {

    public static BidInfoRes from(Bid bid) {
        return new BidInfoRes(
            bid.getId(),
            bid.getBiddingPrice(),
            bid.getBiddingStatus(),
            new BidInfoRes.AuctionItemRes(
                bid.getAuctionItem().getId(),
                bid.getAuctionItem().getName(),
                bid.getAuctionItem().getImageUrl(),
                bid.getAuctionItem().getSize(),
                bid.getAuctionItem().getCategory(),
                bid.getAuctionItem().getBrand(),
                bid.getAuctionItem().getStartPrice(),
                bid.getAuctionItem().getStartTime(),
                bid.getAuctionItem().getEndTime()
            ),
            bid.getReview() == null ? null : new BidInfoRes.ReviewRes(
                bid.getReview().getId(),
                bid.getReview().getContent(),
                bid.getReview().getScore()
            )
        );
    }

    private record AuctionItemRes(
        Long id,
        String name,
        String imageUrl,
        AuctionItemSize size,
        AuctionItemCategory category,
        String brand,
        Long startPrice,
        LocalDateTime startTime,
        LocalDateTime endTime
    ) {

    }

    private record ReviewRes(
        Long id,
        String content,
        Integer score
    ) {

    }
}
