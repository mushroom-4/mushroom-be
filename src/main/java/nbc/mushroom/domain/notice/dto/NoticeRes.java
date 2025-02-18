package nbc.mushroom.domain.notice.dto;

import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.user.entity.User;

public record NoticeRes(
    AuctionItem auctionItem,
    User user,
    Like like
) {

    public NoticeRes(AuctionItem auctionItem,
        User user,
        Like like) {
        this.auctionItem = auctionItem;
        this.user = user;
        this.like = like;
    }
}
