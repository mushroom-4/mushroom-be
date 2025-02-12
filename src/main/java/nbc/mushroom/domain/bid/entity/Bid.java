package nbc.mushroom.domain.bid.entity;

import jakarta.persistence.Column;
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
import nbc.mushroom.domain.common.entity.Timestamped;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.user.entity.User;

@Getter
@Entity
@Table(name = "`bid`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bid extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User bidder;

    @Column(nullable = false)
    private Long biddingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BiddingStatus biddingStatus;

    @Builder
    public Bid(Long id, Product product, User bidder, Long biddingPrice) {
        this.id = id;
        this.product = product;
        this.bidder = bidder;
        this.biddingPrice = biddingPrice;
        this.biddingStatus = BiddingStatus.BIDDING;
    }

    public void updateBiddingPrice(Long biddingPrice) {
        this.biddingPrice = biddingPrice;
    }
}
