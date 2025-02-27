package nbc.mushroom.domain.auction_item.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;

public record SearchAuctionItemRes(
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
    AuctionItemStatus status
) {

    public static SearchAuctionItemRes from(AuctionItem auctionItem) {
        return new SearchAuctionItemRes(
            auctionItem.getId(),
            auctionItem.getName(),
            auctionItem.getDescription(),
            auctionItem.getImageUrl(),
            auctionItem.getSize(),
            auctionItem.getCategory(),
            auctionItem.getBrand(),
            auctionItem.getStartPrice(),
            auctionItem.getStartTime(),
            auctionItem.getEndTime(),
            auctionItem.getStatus()
        );
    }
}
