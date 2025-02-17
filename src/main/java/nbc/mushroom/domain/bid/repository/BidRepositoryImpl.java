package nbc.mushroom.domain.bid.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.bid.entity.QBid.bid;
import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_FOUND;
import static nbc.mushroom.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemBidInfo;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.bid.entity.QBid;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
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
    public Bid findPotentiallySucceededBidByAuctionItem(AuctionItem auctionItem) {
        return queryFactory
            .select(bid)
            .from(bid)
            .where(bid.auctionItem.eq(auctionItem))
            .orderBy(bid.biddingPrice.desc())
            .limit(1)
            .fetchOne();
    }


    @Override
    public List<Bid> findPotentiallyFailedBidsByAuctionItem(AuctionItem auctionItem) {

        QBid subBid = new QBid("subBid");

        return queryFactory
            .select(bid)
            .from(bid)
            .where(
                bid.auctionItem.eq(auctionItem),
                bid.biddingPrice.ne(JPAExpressions
                    .select(subBid.biddingPrice.max())
                    .from(subBid)
                    .where(subBid.auctionItem.eq(auctionItem))
                )
            )
            .fetch();
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
            () -> new CustomException(ExceptionType.BID_NOT_FOUND)
        );
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
    public AuctionItemBidInfo auctionItemBidInfoFind(Long id) {

        return Optional.ofNullable(queryFactory
            .select(Projections.constructor(
                AuctionItemBidInfo.class,
                user.nickname,
                auctionItem.startPrice
            ))
            .from(bid)
            .innerJoin(bid.auctionItem, auctionItem)
            .join(bid.auctionItem.seller, user)
            .where(bid.auctionItem.id.eq(id).and(auctionItem.isDeleted.eq(false)))
            .orderBy(bid.biddingPrice.desc())
            .fetchFirst()
        ).orElseThrow(() -> new CustomException(AUCTION_ITEM_NOT_FOUND));
    }

}
