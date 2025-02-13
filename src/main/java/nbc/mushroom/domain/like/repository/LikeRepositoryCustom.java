package nbc.mushroom.domain.like.repository;

import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.user.entity.User;

public interface LikeRepositoryCustom {

    Like findLikeByUserAndAuctionItem(User user, AuctionItem auctionItem);
}
