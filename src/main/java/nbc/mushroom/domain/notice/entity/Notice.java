package nbc.mushroom.domain.notice.entity;

import static nbc.mushroom.domain.notice.entity.NoticeType.START_TIME;

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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.common.entity.Timestamped;
import nbc.mushroom.domain.like.entity.AuctionItemLike;
import nbc.mushroom.domain.user.entity.User;

@Getter
@Entity
@Table(name = "`notice`")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_item_like_id", nullable = false)
    private AuctionItemLike auctionItemLike;

    @Enumerated(EnumType.STRING)
    @JoinColumn(nullable = false)
    private NoticeType noticeType;

    @Builder
    public Notice(Long id, AuctionItem auctionItem, User user, AuctionItemLike auctionItemLike,
        NoticeType noticeType) {
        this.id = id;
        this.auctionItem = auctionItem;
        this.user = user;
        this.auctionItemLike = auctionItemLike;
        this.noticeType = noticeType;
    }

    public String createMessage() {
        if (noticeType == START_TIME) {
            return String.format("%s님! 좋아요 하신 %s 경매 물품이 10분 후 시작 됩니다! 시작가는 %s원 입니다!",
                user.getNickname(),
                auctionItem.getName(),
                auctionItem.getStartPrice()
            );
        }

        return String.format(
            "%s님! 좋아요 하신 %s 경매 물품이 10분 후 종료 됩니다!",
            user.getNickname(),
            auctionItem.getName()
        );
    }
}
