package nbc.mushroom.domain.notice.dto.response;

import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.like.entity.AuctionItemLike;
import nbc.mushroom.domain.user.entity.User;

public record NoticeRes(
    AuctionItem auctionItem,
    User user,
    AuctionItemLike auctionItemLike
) {

}
