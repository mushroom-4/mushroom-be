package nbc.mushroom.domain.like.repository;

import static nbc.mushroom.domain.like.entity.QLike.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.QAuctionItem;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.user.entity.QUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Like getLikeByUserAndAuctionItem(User user, AuctionItem auctionItem) {
        return queryFactory.select(like)
            .from(like)
            .innerJoin(QUser.user).on(like.user.id.eq(user.getId()))
            .innerJoin(QAuctionItem.auctionItem).on(like.auctionItem.id.eq(auctionItem.getId()))
            .fetchOne();
    }
}
