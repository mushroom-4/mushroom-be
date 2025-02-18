package nbc.mushroom.domain.user.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.like.entity.Like;

public record SearchUserAuctionItemLikeRes(

    Long likeId,
    Long auctionItemId,
    String name,
    String imageUrl,
    Long startPrice,
    LocalDateTime startTime,
    LocalDateTime endTime,
    AuctionItemStatus status
) {

    public SearchUserAuctionItemLikeRes(Like like, AuctionItem AuctionItem) {
        this(
            like.getId(),
            AuctionItem.getId(),
            AuctionItem.getName(),
            AuctionItem.getImageUrl(),
            AuctionItem.getStartPrice(),
            AuctionItem.getStartTime(),
            AuctionItem.getEndTime(),
            AuctionItem.getStatus()
        );
    }
}
