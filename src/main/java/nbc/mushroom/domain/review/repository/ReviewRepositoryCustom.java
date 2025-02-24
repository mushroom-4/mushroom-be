package nbc.mushroom.domain.review.repository;

import nbc.mushroom.domain.review.entity.Review;
import nbc.mushroom.domain.user.entity.User;

public interface ReviewRepositoryCustom {

    Review findByBidderAndAuctionItem_Seller(User loginUser, Long sellerId);
}
