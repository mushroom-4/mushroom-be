package nbc.mushroom.domain.review.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.review.entity.Review;

public record SellerReviewDetailRes(
    Long reviewId,
    String content,
    int score,
    BidRes bid
) {

    public static SellerReviewDetailRes from(Review review) {
        return new SellerReviewDetailRes(
            review.getId(), review.getContent(), review.getScore(), new BidRes(review.getBid()));
    }

    @NoArgsConstructor
    @Getter
    private static class BidRes {

        private String bidderName;
        private Long biddingPrice;
        private Long auctionItemId;
        private String auctionItemName;
        private String auctionItemImageUrl;

        public BidRes(Bid bid) {
            this.bidderName = bid.getBidder().getNickname();
            this.biddingPrice = bid.getBiddingPrice();
            this.auctionItemId = bid.getAuctionItem().getId();
            this.auctionItemName = bid.getAuctionItem().getName();
            this.auctionItemImageUrl = bid.getAuctionItem().getImageUrl();

        }
    }
}
