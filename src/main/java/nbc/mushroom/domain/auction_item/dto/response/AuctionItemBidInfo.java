package nbc.mushroom.domain.auction_item.dto.response;

public record AuctionItemBidInfo(
    String bidderNickname,
    Long maxPrice

) {

    public AuctionItemBidInfo(String bidderNickname, Long maxPrice) {
        this.bidderNickname = bidderNickname;
        this.maxPrice = maxPrice;
    }
}

