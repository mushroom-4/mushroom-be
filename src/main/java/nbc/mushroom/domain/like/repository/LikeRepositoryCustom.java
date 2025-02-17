package nbc.mushroom.domain.like.repository;

import java.util.Optional;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface LikeRepositoryCustom {

    Optional<Like> findLikeByUserAndAuctionItem(User user, AuctionItem auctionItem);

    PageImpl<SearchUserAuctionItemLikeRes> findAuctionItemLikeByUserId(User user,
        Pageable pageable);

}
