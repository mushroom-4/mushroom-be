package nbc.mushroom.domain.auction_item.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.entity.QAuctionItem;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
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
        ).orElseThrow(() -> new CustomException(ExceptionType.AUCTION_ITEM_NOT_FOUND));
    }

    @Override
    public Page<SearchAuctionItemRes> findAllAuctionItems(Pageable pageable) {
        QAuctionItem auctionItem = QAuctionItem.auctionItem;

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
            .where(auctionItem.isDeleted.eq(false))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // 전체 데이터 수를 가져오기 위한 쿼리 (페이징을 위해 필요)
        JPAQuery<Long> countQuery = queryFactory.select(auctionItem.count())
            .from(auctionItem)
            .where(auctionItem.isDeleted.eq(false));

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
                auctionItem.startTime.eq(now),
                auctionItem.isDeleted.isFalse(),
                checkStatus()
            )
            .fetch();
    }

    @Override
    public Page<SearchAuctionItemRes> findAuctionItemsByKeywordAndFiltering(
        String sort, String sortOrder, String keyword, String brand, AuctionItemCategory category,
        AuctionItemSize size, LocalDateTime startDate, LocalDateTime endDate, Long minPrice,
        Long maxPrice,
        Pageable pageable) {

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
            .where(auctionItem.isDeleted.eq(false))
            .orderBy(getSortOrders(pageable)) // 정렬 추가
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = queryFactory.select(auctionItem.count())
            .from(auctionItem)
            .where(
                auctionItem.isDeleted.eq(false),
                eqKeyword(keyword),
                eqBrand(brand),
                eqCategory(category),
                eqSize(size),
                goeStartDate(startDate),
                loeEndDate(endDate),
                goeMinPrice(minPrice),
                loeMaxPrice(maxPrice),
                checkStatus()
            );

        List<SearchAuctionItemRes> content = query.fetch();

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?>[] getSortOrders(Pageable pageable) {
        if (!pageable.getSort().isSorted()) {
            return new OrderSpecifier[]{auctionItem.name.asc()};

        }

        // 여러 정렬 조건 처리
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
                // 좋아요 정렬 순은 이후 추가해야 될 듯 합니다...
                default -> auctionItem.name.asc();
            };
            orders.add(orderSpecifier);
        }

        return orders.toArray(new OrderSpecifier[]{});
    }

    // 동적 쿼리
    private BooleanExpression eqKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        return auctionItem.name.eq(keyword);
    }

    private BooleanExpression eqBrand(String brand) {
        if (brand == null) {
            return null;
        }
        return auctionItem.brand.eq(brand);
    }

    private BooleanExpression eqCategory(AuctionItemCategory category) {
        if (category == null) {
            return null;
        }
        return auctionItem.category.eq(category);
    }

    private BooleanExpression eqSize(AuctionItemSize size) {
        if (size == null) {
            return null;
        }
        return auctionItem.size.eq(size);
    }

    private BooleanExpression goeStartDate(LocalDateTime startDate) {
        if (startDate == null) {
            return null;
        }
        return auctionItem.startTime.goe(startDate);
    }

    private BooleanExpression loeEndDate(LocalDateTime endDate) {
        if (endDate == null) {
            return null;
        }
        return auctionItem.startTime.loe(endDate); // endDate보다 전이거나 같은 startTime 조건 반환
    }

    private BooleanExpression goeMinPrice(Long minPrice) {
        if (minPrice == null) {
            return null;
        }
        return auctionItem.startPrice.goe(minPrice); // startPrice가 minPrice보다 크거나 같은 조건 반환
    }

    private BooleanExpression loeMaxPrice(Long maxPrice) {
        if (maxPrice == null) {
            return null;
        }
        return auctionItem.startPrice.loe(maxPrice); // startPrice가 maxPrice보다 작거나 같은 조건 반환
    }

    private BooleanExpression checkStatus() {
        return auctionItem.status.eq(AuctionItemStatus.WAITING)
            .or(auctionItem.status.eq(AuctionItemStatus.PROGRESSING));
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
                auctionItem.endTime.eq(now),
                auctionItem.isDeleted.isFalse()
            )
            .fetch();
    }
}
