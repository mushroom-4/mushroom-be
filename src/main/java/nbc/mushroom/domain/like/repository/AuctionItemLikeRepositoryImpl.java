package nbc.mushroom.domain.like.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.like.entity.QAuctionItemLike.auctionItemLike;
import static nbc.mushroom.domain.user.entity.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.QAuctionItem;
import nbc.mushroom.domain.like.entity.AuctionItemLike;
import nbc.mushroom.domain.notice.dto.NoticeRes;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.entity.QUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class AuctionItemLikeRepositoryImpl implements AuctionItemLikeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<AuctionItemLike> findLikeByUserAndAuctionItem(User user,
        AuctionItem auctionItem) {
        return Optional.ofNullable(queryFactory.select(auctionItemLike)
            .from(auctionItemLike)
            .innerJoin(QUser.user).on(auctionItemLike.user.id.eq(user.getId()))
            .innerJoin(QAuctionItem.auctionItem)
            .on(auctionItemLike.auctionItem.id.eq(auctionItem.getId()))
            .fetchOne());

    }

    @Override
    public PageImpl<SearchUserAuctionItemLikeRes> findAuctionItemLikeByUserId(User user,
        Pageable pageable) {

        List<SearchUserAuctionItemLikeRes> searchUserAuctionItemLikeResList =
            queryFactory.select(Projections.constructor(
                    SearchUserAuctionItemLikeRes.class,
                    auctionItemLike,
                    auctionItem))
                .from(auctionItemLike)
                .innerJoin(auctionItemLike.user, QUser.user)
                .innerJoin(auctionItemLike.auctionItem, auctionItem)
                .where(auctionItemLike.user.id.eq(user.getId()),
                    auctionItem.isDeleted.eq(false))
                .orderBy(getSortOrders(pageable)) // 정렬 추가
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = Optional.ofNullable(
            queryFactory.select(Wildcard.count)
                .from(auctionItemLike)
                .innerJoin(auctionItemLike.user, QUser.user)
                .innerJoin(auctionItemLike.auctionItem, auctionItem)
                .where(auctionItemLike.user.id.eq(user.getId()),
                    auctionItem.isDeleted.eq(false))
                .fetchOne()).orElse(0L);

        return new PageImpl<>(searchUserAuctionItemLikeResList, pageable, totalCount);
    }

    public List<NoticeRes> findNoticeInfoOfStartByAuctionItemLike(LocalDateTime now,
        LocalDateTime nowPlus10) {
        return queryFactory
            .select(Projections.constructor(NoticeRes.class,
                auctionItem, user, auctionItemLike))
            .from(auctionItemLike)
            .innerJoin(auctionItemLike.auctionItem, auctionItem)
            .innerJoin(auctionItemLike.user, user)
            .fetchJoin()
            .where(
                // 현재 시간과 startTime 비교 // 현재 시간+10분 과 startTime 비교
                auctionItem.startTime.gt(now).and(auctionItem.startTime.loe(nowPlus10)),
                auctionItem.isDeleted.isFalse() // 삭제되지 않은 항목만 검색
            ).fetch();
    }

    @Override
    public List<NoticeRes> findNoticeInfoOfEndByAuctionItemLike(LocalDateTime now,
        LocalDateTime nowPlus10) {
        return queryFactory
            .select(Projections.constructor(NoticeRes.class,
                auctionItem, user, auctionItemLike))
            .from(auctionItemLike)
            .innerJoin(auctionItemLike.auctionItem, auctionItem)
            .innerJoin(auctionItemLike.user, user)
            .where(
                // 현재 시간과 endTime 비교 // 현재 시간+10분 과 endTime 비교
                auctionItem.endTime.gt(now).and(auctionItem.endTime.loe(nowPlus10)),
                auctionItem.isDeleted.isFalse() // 삭제되지 않은 항목만 검색
            ).fetch();
    }

    // like.id 오름 차순으로 정렬
    private OrderSpecifier<?>[] getSortOrders(Pageable pageable) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Order order : pageable.getSort()) {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            OrderSpecifier<?> orderSpecifier =
                isAscending ? auctionItemLike.id.asc() : auctionItemLike.id.desc();
            orders.add(orderSpecifier);
        }
        return orders.toArray(new OrderSpecifier[]{});
    }
}
