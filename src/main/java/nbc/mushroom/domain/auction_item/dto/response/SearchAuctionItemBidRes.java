package nbc.mushroom.domain.auction_item.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.review.entity.Review;
import nbc.mushroom.domain.user.entity.User;

public record SearchAuctionItemBidRes(
    Long auctionItemId,
    String name,
    String description,
    String imageUrl,
    AuctionItemSize size,
    AuctionItemCategory category,
    String brand,
    Long startPrice,
    LocalDateTime startTime,
    LocalDateTime endTime,
    AuctionItemStatus status,
    AuctionItemBidInfoRes bid,
    SellerRes seller
) {

    public static SearchAuctionItemBidRes from(AuctionItem searchAuctionItem,
        AuctionItemBidInfoRes bid, List<Review> reviews) {
        return new SearchAuctionItemBidRes(
            searchAuctionItem.getId(),
            searchAuctionItem.getName(),
            searchAuctionItem.getDescription(),
            searchAuctionItem.getImageUrl(),
            searchAuctionItem.getSize(),
            searchAuctionItem.getCategory(),
            searchAuctionItem.getBrand(),
            searchAuctionItem.getStartPrice(),
            searchAuctionItem.getStartTime(),
            searchAuctionItem.getEndTime(),
            searchAuctionItem.getStatus(),
            bid,
            new SellerRes(searchAuctionItem.getSeller(), reviews)
        );
    }

    @NoArgsConstructor
    @Getter
    private static class SellerRes {

        private Long id;
        private String nickname;
        private String imageUrl;
        private Double averageScore;
        private Integer totalReviewCount;

        public SellerRes(User seller, List<Review> reviews) {
            this.id = seller.getId();
            this.nickname = seller.getNickname();
            this.imageUrl = seller.getImageUrl();
            this.averageScore = reviews.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
            this.totalReviewCount = reviews.size();

        }
    }
}
