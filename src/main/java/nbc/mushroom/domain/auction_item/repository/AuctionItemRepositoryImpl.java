package nbc.mushroom.domain.auction_item.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.entity.QAuctionItem;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
                auctionItem.isDeleted.isFalse()
            )
            .fetch();
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
