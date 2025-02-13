package nbc.mushroom.domain.auction_item.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;

public record AuctionItemRes(Long auctionItemId, String name,
                             String description, String imageUrl,
                             AuctionItemSize size, AuctionItemCategory category,
                             String brand, Long startPrice,
                             LocalDateTime startTime, LocalDateTime endTime,
                             AuctionItemStatus auctionItemStatus
) {

    public static AuctionItemRes from(AuctionItem searchAuctionItem, String imageUrl) {
        return new AuctionItemRes(
            searchAuctionItem.getId(),
            searchAuctionItem.getName(),
            searchAuctionItem.getDescription(),
            imageUrl,
            searchAuctionItem.getSize(),
            searchAuctionItem.getCategory(),
            searchAuctionItem.getBrand(),
            searchAuctionItem.getStartPrice(),
            searchAuctionItem.getStartTime(),
            searchAuctionItem.getEndTime(),
            searchAuctionItem.getStatus()
        );
    }
}
