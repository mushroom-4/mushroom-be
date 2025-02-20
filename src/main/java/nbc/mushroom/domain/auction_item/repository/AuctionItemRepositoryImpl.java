package nbc.mushroom.domain.auction_item.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_FOUND;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.admin.dto.response.AuctionItemStatusRes;
import nbc.mushroom.domain.admin.dto.response.QAuctionItemStatusRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.entity.QAuctionItem;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionItemRepositoryImpl implements AuctionItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public AuctionItem findAuctionItemById(Long id) {
        return Optional.ofNullable(queryFactory
            .select(auctionItem)
            .from(auctionItem)
            .where(auctionItem.id.eq(id).and(auctionItem.isDeleted.eq(false)))
            .fetchOne()
        ).orElseThrow(() -> new CustomException(AUCTION_ITEM_NOT_FOUND));
    }

    @Override
    public Page<SearchAuctionItemRes> findAllAuctionItems(Pageable pageable) {
        QAuctionItem auctionItem = QAuctionItem.auctionItem;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(
            auctionItem.status.eq(AuctionItemStatus.WAITING)
                .or(auctionItem.status.eq(AuctionItemStatus.PROGRESSING))
        );

        JPAQuery<SearchAuctionItemRes> query = queryFactory
            .select(Projections.constructor(SearchAuctionItemRes.class,
                auctionItem.id,
                auctionItem.name,
                auctionItem.description,
                auctionItem.imageUrl,
                auctionItem.size,
                auctionItem.category,
                auctionItem.brand,
                auctionItem.startPrice,
                auctionItem.startTime,
                auctionItem.endTime,
                auctionItem.status
            ))
            .from(auctionItem)
            .where(auctionItem.isDeleted.eq(false), builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // 전체 데이터 수를 가져오기 위한 쿼리 (페이징을 위해 필요)
        JPAQuery<Long> countQuery = queryFactory.select(auctionItem.count())
            .from(auctionItem)
            .where(auctionItem.isDeleted.eq(false), builder);

        // 쿼리 실행 후, 결과 데이터를 리스트로 반환
        List<SearchAuctionItemRes> content = query.fetch();

        // Page 객체를 만들어서 반환
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<AuctionItem> findAuctionItemsByStatusAndStartTime(
        AuctionItemStatus auctionItemStatus,
        LocalDateTime now) {
        return queryFactory.select(auctionItem)
            .from(auctionItem)
            .where(
                auctionItem.status.eq(auctionItemStatus),
                auctionItem.startTime.between(now, now.withSecond(59)),
                auctionItem.isDeleted.isFalse()
            )
            .fetch();
    }

    @Override
    public Page<SearchAuctionItemRes> findAuctionItemsByKeywordAndFiltering(
        String sort, String sortOrder, String keyword, String brand, AuctionItemCategory category,
        AuctionItemSize size, LocalDateTime startDate, LocalDateTime endDate, Long minPrice,
        Long maxPrice, Pageable pageable) {

        BooleanBuilder builder = auctionItemsBuilder(
            keyword, brand, category, size, startDate, endDate, minPrice, maxPrice
        );

        JPAQuery<SearchAuctionItemRes> query = queryFactory
            .select(Projections.constructor(SearchAuctionItemRes.class,
                auctionItem.id,
                auctionItem.name,
                auctionItem.description,
                auctionItem.imageUrl,
                auctionItem.size,
                auctionItem.category,
                auctionItem.brand,
                auctionItem.startPrice,
                auctionItem.startTime,
                auctionItem.endTime,
                auctionItem.status
            ))
            .from(auctionItem)
            .where(auctionItem.isDeleted.eq(false), builder)
            .orderBy(getSortOrders(pageable))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory.select(auctionItem.count())
            .from(auctionItem)
            .where(auctionItem.isDeleted.eq(false), builder);

        List<SearchAuctionItemRes> content = query.fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public boolean existsByUserAndAuctionItem(User user, Long auctionItemId) {
        return queryFactory.select(auctionItem)
            .from(auctionItem)
            .where(QAuctionItem.auctionItem.seller.id.eq(user.getId())
                .and(auctionItem.id.eq(auctionItemId)))
            .fetchOne() != null;

    }

    @Override
    public List<AuctionItem> findAuctionItemsByStatusAndEndTime(AuctionItemStatus auctionItemStatus,
        LocalDateTime now) {
        return queryFactory.select(auctionItem)
            .from(auctionItem)
            .where(
                auctionItem.status.eq(auctionItemStatus),
                auctionItem.endTime.between(now, now.withSecond(59)),
                auctionItem.isDeleted.isFalse()
            )
            .fetch();
    }

    // 판매자 유저 ID로 등록된 경매 물품 조회 메서드
    @Override
    public Page<AuctionItem> findRegisteredAuctionItemsByUserId(Long userId, Pageable pageable) {
        JPAQuery<AuctionItem> query = queryFactory
            .selectFrom(auctionItem)
            .where(
                auctionItem.seller.id.eq(userId),
                auctionItem.isDeleted.isFalse()
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory
            .select(auctionItem.count())
            .from(auctionItem)
            .where(
                auctionItem.seller.id.eq(userId),
                auctionItem.isDeleted.isFalse()
            );

        List<AuctionItem> content = query.fetch();
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 경매 물품 상태별 목록 필터링 조회 메서드
    @Override
    public Page<AuctionItemStatusRes> findAuctionItemsByStatus(
        List<AuctionItemStatus> status,
        Pageable pageable
    ) {
        QAuctionItem auctionItem = QAuctionItem.auctionItem;
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null && !status.isEmpty()) {
            builder.and(auctionItem.status.in(status));
        }

        List<AuctionItemStatusRes> results = queryFactory
            .select(new QAuctionItemStatusRes(
                auctionItem.id,
                auctionItem.name,
                auctionItem.description,
                auctionItem.imageUrl,
                auctionItem.size,
                auctionItem.category,
                auctionItem.brand,
                auctionItem.startPrice,
                auctionItem.startTime,
                auctionItem.endTime,
                auctionItem.status
            ))
            .from(auctionItem)
            .where(builder)
            .orderBy(auctionItem.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> total = queryFactory
            .select(auctionItem.count())
            .from(auctionItem)
            .where(builder);

        return PageableExecutionUtils.getPage(results, pageable, total::fetchOne);
    }

    private OrderSpecifier<?>[] getSortOrders(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            return new OrderSpecifier[]{auctionItem.name.asc()};
        }

        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();
            boolean isAscending = order.isAscending();

            OrderSpecifier<?> orderSpecifier = switch (property) {
                case "name" -> isAscending ? auctionItem.name.asc() : auctionItem.name.desc();
                case "brand" -> isAscending ? auctionItem.brand.asc() : auctionItem.brand.desc();
                case "category" ->
                    isAscending ? auctionItem.category.asc() : auctionItem.category.desc();
                case "size" -> isAscending ? auctionItem.size.asc() : auctionItem.size.desc();
                case "startTime" ->
                    isAscending ? auctionItem.startTime.asc() : auctionItem.startTime.desc();
                case "endTime" ->
                    isAscending ? auctionItem.endTime.asc() : auctionItem.endTime.desc();
                case "startPrice" ->
                    isAscending ? auctionItem.startPrice.asc() : auctionItem.startPrice.desc();
                case "status" -> isAscending ? auctionItem.status.asc() : auctionItem.status.desc();

                default -> auctionItem.name.asc();
            };
            orders.add(orderSpecifier);
        }

        return orders.toArray(new OrderSpecifier[0]);
    }

    // 동적 쿼리 Boolean Builder 사용
    private BooleanBuilder auctionItemsBuilder(
        String keyword, String brand, AuctionItemCategory category,
        AuctionItemSize size, LocalDateTime startDate, LocalDateTime endDate,
        Long minPrice, Long maxPrice) {

        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            builder.or(auctionItem.name.contains(keyword)
                .or(auctionItem.description.contains(keyword))
                .or(auctionItem.brand.contains(keyword)));
        }

        if (brand != null && !brand.isBlank()) {
            builder.and(auctionItem.brand.eq(brand));
        }

        if (category != null) {
            builder.and(auctionItem.category.eq(category));
        }

        if (size != null) {
            builder.and(auctionItem.size.eq(size));
        }

        if (startDate != null) {
            builder.and(auctionItem.startTime.goe(startDate));
        }

        if (endDate != null) {
            builder.and(auctionItem.startTime.loe(endDate));
        }

        if (minPrice != null) {
            builder.and(auctionItem.startPrice.goe(minPrice));
        }

        if (maxPrice != null) {
            builder.and(auctionItem.startPrice.loe(maxPrice));
        }

        builder.and(
            auctionItem.status.eq(AuctionItemStatus.WAITING)
                .or(auctionItem.status.eq(AuctionItemStatus.PROGRESSING))
        );
        return builder;
    }
}
