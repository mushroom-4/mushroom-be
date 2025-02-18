package nbc.mushroom.domain.notice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    // 고민 - 필요한가? OneToOne??
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "like_id", nullable = false)
    private Like like;

    @Builder
    public Notice(Long id, AuctionItem auctionItem, User user, Like like) {
        this.id = id;
        this.auctionItem = auctionItem;
        this.user = user;
        this.like = like;
    }
}
