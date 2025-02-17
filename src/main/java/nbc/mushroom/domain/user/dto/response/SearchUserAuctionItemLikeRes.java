package nbc.mushroom.domain.user.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;

public record SearchUserAuctionItemLikeRes(

    Long likeId,
    Long auctionItemId,
    String name,
//    String description,
    String imageUrl,
//    AuctionItemSize size,
//    AuctionItemCategory category,
//    String brand,
    Long startPrice,
    LocalDateTime startTime,
    LocalDateTime endTime,
    AuctionItemStatus status
) {

    public static SearchUserAuctionItemLikeRes from(Long likeId, AuctionItem searchAuctionItem) {
        return new SearchUserAuctionItemLikeRes(
            likeId,
            searchAuctionItem.getId(),
            searchAuctionItem.getName(),
            searchAuctionItem.getImageUrl(),
            searchAuctionItem.getStartPrice(),
            searchAuctionItem.getStartTime(),
            searchAuctionItem.getEndTime(),
            searchAuctionItem.getStatus()
        );
    }
}
