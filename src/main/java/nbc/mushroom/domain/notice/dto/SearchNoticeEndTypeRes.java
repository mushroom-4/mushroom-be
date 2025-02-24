package nbc.mushroom.domain.notice.dto;

public record SearchNoticeEndTypeRes(
    Long maxPrice,
    Long auctionId

) {

    public SearchNoticeEndTypeRes(Long maxPrice, Long auctionId) {
        this.maxPrice = maxPrice;
        this.auctionId = auctionId;
    }
}

