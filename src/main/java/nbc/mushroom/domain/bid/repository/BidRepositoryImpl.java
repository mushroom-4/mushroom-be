package nbc.mushroom.domain.bid.repository;

import static nbc.mushroom.domain.bid.entity.QBid.bid;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Bid> findBidByUserAndProduct(User bidder, Product product) {
        return Optional.ofNullable(
            queryFactory
                .select(bid)
                .from(bid)
                .where(
                    bid.product.eq(product),
                    bid.bidder.eq(bidder)
                )
                .fetchOne()
        );
    }
}
