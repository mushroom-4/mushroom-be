package nbc.mushroom.domain.like.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.like.entity.QLike.like;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.QAuctionItem;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.entity.QUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Like> findLikeByUserAndAuctionItem(User user, AuctionItem auctionItem) {
        return Optional.ofNullable(queryFactory.select(like)
            .from(like)
            .innerJoin(QUser.user).on(like.user.id.eq(user.getId()))
            .innerJoin(QAuctionItem.auctionItem).on(like.auctionItem.id.eq(auctionItem.getId()))
            .fetchOne());

    }

    @Override
    public PageImpl<SearchUserAuctionItemLikeRes> findAuctionItemLikeByUserId(User user,
        Pageable pageable) {

        List<SearchUserAuctionItemLikeRes> searchUserAuctionItemLikeResList =
            queryFactory.select(Projections.constructor(
                    SearchUserAuctionItemLikeRes.class,
                    like,
                    auctionItem))
                .from(like)
                .innerJoin(like.user, QUser.user)
                .innerJoin(like.auctionItem, auctionItem)
                .where(like.user.id.eq(user.getId()),
                    auctionItem.isDeleted.eq(false))
                .orderBy(getSortOrders(pageable)) // 정렬 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = Optional.ofNullable(
            queryFactory.select(Wildcard.count)
                .from(like)
                .innerJoin(like.user, QUser.user)
                .innerJoin(like.auctionItem, auctionItem)
                .where(like.user.id.eq(user.getId()),
                    auctionItem.isDeleted.eq(false))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(searchUserAuctionItemLikeResList, pageable, totalCount);
    }

    // like.id 오름 차순으로 정렬
    private OrderSpecifier<?>[] getSortOrders(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            OrderSpecifier<?> orderSpecifier = isAscending ? like.id.asc() : like.id.desc();
            orders.add(orderSpecifier);
        }
        return orders.toArray(new OrderSpecifier[]{});
    }
}
