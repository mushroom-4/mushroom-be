package nbc.mushroom.domain.like.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;

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

}
