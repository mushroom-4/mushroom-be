package nbc.mushroom.domain.auction_item.entity;

import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.COMPLETED;
import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.INSPECTING;
import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.PROGRESSING;
import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.WAITING;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbc.mushroom.domain.common.entity.Timestamped;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.user.entity.User;

@Getter
@Entity
@Table(name = "`auction_item`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionItem extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User seller;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url", length = 150)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private AuctionItemSize size;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private AuctionItemCategory category;

    @Column(name = "brand", length = 50, nullable = false)
    private String brand;

    @Column(name = "start_price", nullable = false)
    private Long startPrice;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuctionItemStatus status = INSPECTING;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Builder
    public AuctionItem(Long id, User seller, String name, String description, String imageUrl,
        AuctionItemSize size, AuctionItemCategory category, String brand, Long startPrice,
        LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.size = size;
        this.category = category;
        this.brand = brand;
        this.startPrice = startPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void approve() {
        if (this.status != AuctionItemStatus.INSPECTING) {
            throw new CustomException(ExceptionType.AUCTION_ITEM_ALREADY_INSPECTED);
        }

        this.status = AuctionItemStatus.WAITING;
    }

    public void reject() {
        if (this.status != AuctionItemStatus.INSPECTING) {
            throw new CustomException(ExceptionType.AUCTION_ITEM_ALREADY_INSPECTED);
        }

        this.status = AuctionItemStatus.REJECTED;
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void start() {
        if (this.status != WAITING) {
            throw new CustomException(ExceptionType.INVALID_AUCTION_ITEM_STATUS);
        }
        this.status = AuctionItemStatus.PROGRESSING;
    }

    public void completed() {
        if (this.status != PROGRESSING) {
            throw new CustomException(ExceptionType.INVALID_AUCTION_ITEM_STATUS);
        }
        this.status = COMPLETED;
    }
}
