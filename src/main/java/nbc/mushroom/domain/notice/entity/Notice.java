package nbc.mushroom.domain.notice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.common.entity.Timestamped;
import nbc.mushroom.domain.like.entity.Like;
import nbc.mushroom.domain.user.entity.User;

@Getter
@Entity
@Table(name = "`notice`",
    uniqueConstraints = @UniqueConstraint(columnNames = {"like_id", "notice_type"}))
// 유니크 제약 조건에서 notice_type 추가 //Todo 올릴때 지우기
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_item_id", nullable = false)
    private AuctionItem auctionItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Todo notice bid 에 필요없으면 지우자
    @ManyToOne(fetch = FetchType.LAZY) // 불필요 - 공지 생성시 like 를 디져서 만들기에...
    @JoinColumn(name = "like_id", nullable = false)
    private Like like;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    private NoticeType noticeType;

    @Builder
    public Notice(Long id, AuctionItem auctionItem, User user, Like like, NoticeType noticeType) {
        this.id = id;
        this.auctionItem = auctionItem;
        this.user = user;
        this.like = like;
        this.noticeType = noticeType;
    }
}
