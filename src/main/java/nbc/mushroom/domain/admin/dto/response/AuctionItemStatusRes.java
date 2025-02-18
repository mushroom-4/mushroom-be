package nbc.mushroom.domain.admin.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;

public record AuctionItemStatusRes(
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

    // QueryProjection 애너테이션을 사용하기 위해 추가한 생성자
    @QueryProjection
    public AuctionItemStatusRes(
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
        this.auctionItemId = auctionItemId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.size = size;
        this.category = category;
        this.brand = brand;
        this.startPrice = startPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}

