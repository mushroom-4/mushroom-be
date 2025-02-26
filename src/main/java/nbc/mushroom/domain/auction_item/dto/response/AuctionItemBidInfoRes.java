package nbc.mushroom.domain.auction_item.dto.response;

public record AuctionItemBidInfoRes(
    String bidderNickname,
    Long maxPrice,
    String imageUrl

) {

    public AuctionItemBidInfoRes(String bidderNickname, Long maxPrice, String imageUrl) {
        this.bidderNickname = bidderNickname;
        this.maxPrice = maxPrice;
        this.imageUrl = imageUrl;
    }
}

