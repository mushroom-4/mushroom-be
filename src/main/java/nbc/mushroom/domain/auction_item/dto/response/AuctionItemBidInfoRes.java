package nbc.mushroom.domain.auction_item.dto.response;

public record AuctionItemBidInfoRes(
    String bidderNickname,
    Long maxPrice

) {

    public AuctionItemBidInfoRes(String bidderNickname, Long maxPrice) {
        this.bidderNickname = bidderNickname;
        this.maxPrice = maxPrice;
    }
}

