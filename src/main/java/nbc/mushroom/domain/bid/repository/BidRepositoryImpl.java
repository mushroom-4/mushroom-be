package nbc.mushroom.domain.bid.repository;

import static nbc.mushroom.domain.bid.entity.QBid.bid;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.QBid;
import nbc.mushroom.domain.user.entity.User;
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
            .select(bid.count())
            .from(bid)
            .where(bid.auctionItem.eq(auctionItem))
            .fetchOne() != null;
    }
}
