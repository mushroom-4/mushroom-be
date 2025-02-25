package nbc.mushroom.domain.like.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.like.entity.AuctionItemLike;

public record LikedAuctionItemRes(

    Long auctionItemLikeId,
    Long auctionItemId,
    String name,
    String imageUrl,
    Long startPrice,
    LocalDateTime startTime,
    LocalDateTime endTime,
    AuctionItemStatus status
) {

    public LikedAuctionItemRes(AuctionItemLike auctionItemLike, AuctionItem AuctionItem) {
        this(
            auctionItemLike.getId(),
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
