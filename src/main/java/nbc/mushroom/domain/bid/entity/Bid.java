package nbc.mushroom.domain.bid.entity;

import static nbc.mushroom.domain.bid.entity.BiddingStatus.BIDDING;
import static nbc.mushroom.domain.bid.entity.BiddingStatus.SUCCEED;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_BID_STATUS;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_PAYMENT_AMOUNT;

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
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.common.entity.Timestamped;
import nbc.mushroom.domain.common.exception.CustomException;
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
    @JoinColumn(name = "auction_item_id", nullable = false)
    private AuctionItem auctionItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User bidder;

    @Column(nullable = false)
    private Long biddingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BiddingStatus biddingStatus;

    @Builder
    public Bid(Long id, AuctionItem auctionItem, User bidder, Long biddingPrice) {
        this.id = id;
        this.auctionItem = auctionItem;
        this.bidder = bidder;
        this.biddingPrice = biddingPrice;
        this.biddingStatus = BIDDING;
    }

    public void updateBiddingPrice(Long biddingPrice) {
        this.biddingPrice = biddingPrice;
    }

    public void fail() {
        if (this.biddingStatus != BIDDING) {
            throw new CustomException(INVALID_BID_STATUS);
        }
        this.biddingStatus = BiddingStatus.FAILED;
    }

    public void succeed() {
        if (this.biddingStatus != BIDDING) {
            throw new CustomException(INVALID_BID_STATUS);
        }
        this.biddingStatus = BiddingStatus.SUCCEED;
    }

    public void cancel() {
        if (this.biddingStatus != BIDDING) {
            throw new CustomException(INVALID_BID_STATUS);
        }
        this.biddingStatus = BiddingStatus.CANCELED;
    }

    public void paymentComplete(Long paymentAmount) {
        if (!biddingPrice.equals(paymentAmount)) {
            throw new CustomException(INVALID_PAYMENT_AMOUNT);
        }
        
        if (this.biddingStatus != SUCCEED) {
            throw new CustomException(INVALID_BID_STATUS);
        }
        this.biddingStatus = BiddingStatus.PAYMENT_COMPLETED;
    }
}
