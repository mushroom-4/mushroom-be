package nbc.mushroom.domain.review.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.bid.entity.QBid.bid;
import static nbc.mushroom.domain.review.entity.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.review.entity.Review;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Review findByBidderAndAuctionItem_Seller(User loginUser, Long sellerId) {
        return queryFactory
            .select(review)
            .from(review)
            .join(review.bid, bid)
            .join(bid.auctionItem, auctionItem)
            .where(
                auctionItem.seller.id.eq(sellerId),
                bid.bidder.eq(loginUser),
                bid.biddingStatus.eq(BiddingStatus.PAYMENT_COMPLETED)
            )
            .limit(1)
            .fetchOne();
    }

}
