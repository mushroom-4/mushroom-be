package nbc.mushroom.domain.review.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.bid.entity.QBid.bid;
import static nbc.mushroom.domain.review.entity.QReview.review;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.review.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Review> findAllBySellerId(Long sellerId) {
        return queryFactory
            .selectFrom(review)
            .innerJoin(review.bid, bid)
            .innerJoin(bid.auctionItem, auctionItem)
            .where(auctionItem.seller.id.eq(sellerId))
            .fetch();
    }

    @Override
    public Review findByBidIdAndUserId(Long bidId, Long id) {
        return queryFactory
            .selectFrom(review)
            .innerJoin(review.bid, bid)
            .where(bid.id.eq(bidId)
                .and(bid.bidder.id.eq(id)))
            .fetchOne();
    }

}