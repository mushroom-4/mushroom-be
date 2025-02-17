package nbc.mushroom.domain.auction_item.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;

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
    AuctionItemBidInfo bid
) {

    public static SearchAuctionItemBidRes from(AuctionItem searchAuctionItem,
        AuctionItemBidInfo bid) {
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
            bid
        );
    }

}
