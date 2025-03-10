package nbc.mushroom.domain.bid.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.bid.entity.QBid.bid;
import static nbc.mushroom.domain.common.exception.ExceptionType.BID_NOT_FOUND;
import static nbc.mushroom.domain.review.entity.QReview.review;
import static nbc.mushroom.domain.user.entity.QUser.user;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemBidInfoRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.dto.response.BidInfoRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Bid> findBidByUserAndAuctionItem(User bidder, AuctionItem auctionItem) {
        return Optional.ofNullable(
            queryFactory
                .select(bid)
                .from(bid)
                .where(
                    bid.auctionItem.eq(auctionItem),
                    bid.bidder.eq(bidder)
                )
                .fetchOne()
        );
    }

    @Override
    public Optional<Bid> findMaxPriceBidInAuctionItem(AuctionItem auctionItem) {
        return Optional.ofNullable(queryFactory
            .select(bid)
            .from(bid)
            .where(
                bid.auctionItem.eq(auctionItem),
                bid.biddingStatus.eq(BiddingStatus.BIDDING)
            )
            .orderBy(bid.biddingPrice.desc())
            .limit(1)
            .fetchOne()
        );
    }

    @Override
    public Boolean existsBidByAuctionItem(AuctionItem auctionItem) {
        return queryFactory
            .select(bid)
            .from(bid)
            .where(bid.auctionItem.eq(auctionItem))
            .fetchFirst() != null;
    }

    @Override
    public Page<Bid> findBidsByUser(User user, Pageable pageable) {

        List<Bid> content = queryFactory
            .select(bid)
            .from(bid)
            .where(bid.bidder.eq(user))
            .orderBy(bid.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> queryCount = queryFactory
            .select(bid.count())
            .from(bid)
            .where(bid.bidder.eq(user));

        return PageableExecutionUtils.getPage(content, pageable, queryCount::fetchOne);
    }

    @Override
    public Bid findBidByBidderAndId(User bidder, Long bidId) {
        Optional<Bid> optionalBid = Optional.ofNullable(queryFactory
            .select(bid)
            .from(bid)
            .where(bid.bidder.eq(bidder),
                bid.id.eq(bidId))
            .fetchOne()
        );

        return optionalBid.orElseThrow(
            () -> new CustomException(BID_NOT_FOUND)
        );
    }

    @Override
    public BidInfoRes findBidInfoByBidderAndId(User bidder, Long bidId) {
        Tuple result = queryFactory
            .select(
                bid,
                review
            )
            .from(bid)
            .innerJoin(bid.auctionItem, auctionItem)
            .leftJoin(review).on(review.bid.eq(bid))
            .where(
                bid.bidder.eq(bidder),
                bid.id.eq(bidId)
            )
            .fetchOne();

        if (result == null) {
            throw new CustomException(BID_NOT_FOUND);
        }

        return BidInfoRes.from(Objects.requireNonNull(result.get(bid)), result.get(review));
    }

    @Override
    public Long countBidsByBidderAndStatus(User bidder, BiddingStatus biddingStatus) {
        return queryFactory
            .select(bid.count())
            .from(bid)
            .where(bid.bidder.eq(bidder),
                bid.biddingStatus.eq(biddingStatus))
            .fetchOne();
    }

    @Override
    public AuctionItemBidInfoRes auctionItemBidInfoFind(Long auctionItemId) {

        return Optional.ofNullable(queryFactory
            .select(Projections.constructor(
                AuctionItemBidInfoRes.class,
                bid.bidder.nickname,
                bid.biddingPrice,
                bid.bidder.imageUrl
            ))
            .from(bid)
            .innerJoin(bid.auctionItem, auctionItem)
            .innerJoin(bid.auctionItem.seller, user)
            .where(bid.auctionItem.id.eq(auctionItemId).and(auctionItem.isDeleted.eq(false)))
            .orderBy(bid.biddingPrice.desc())
            .fetchFirst()
        ).orElseThrow(() -> new CustomException(BID_NOT_FOUND));
    }

    @Override
    public Boolean existBidByBidderIdAndAuctionItemId(Long bidderId, Long auctionItemId) {
        return queryFactory
            .select(bid)
            .from(bid)
            .where(
                bid.auctionItem.id.eq(auctionItemId),
                bid.bidder.id.eq(bidderId))
            .fetchFirst() != null;
    }
}
