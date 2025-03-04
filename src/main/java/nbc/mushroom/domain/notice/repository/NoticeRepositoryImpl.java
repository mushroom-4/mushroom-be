package nbc.mushroom.domain.notice.repository;

import static nbc.mushroom.domain.auction_item.entity.QAuctionItem.auctionItem;
import static nbc.mushroom.domain.notice.entity.QNotice.notice;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.notice.entity.Notice;
import nbc.mushroom.domain.user.entity.QUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notice> findAllNoticeResByUser(User user) {
        return queryFactory
            .select(notice)
            .from(notice)
            .leftJoin(notice.auctionItem, auctionItem).fetchJoin()
            .leftJoin(notice.user, QUser.user).fetchJoin()
            .where(
                QUser.user.eq(user),
                auctionItem.isDeleted.eq(false),
                notice.auctionItem.eq(auctionItem))
            .orderBy(notice.createdAt.desc())
            .fetch();
    }
}
