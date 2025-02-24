package nbc.mushroom.domain.notice.dto;

import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.notice.entity.Notice;
import nbc.mushroom.domain.user.entity.User;

public record SearchNoticeRes(
    AuctionItem auctionItem,
    User user,
    Notice notice
) {

}
