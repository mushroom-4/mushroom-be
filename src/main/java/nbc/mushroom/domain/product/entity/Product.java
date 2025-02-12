package nbc.mushroom.domain.product.entity;

import static nbc.mushroom.domain.product.entity.ProductStatus.INSPECTING;

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
import nbc.mushroom.domain.user.entity.User;

@Getter
@Entity
@Table(name = "`product`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {

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
    private String image_url;

    @Enumerated(EnumType.STRING)
    @Column(name = "size", nullable = false)
    private ProductSize size;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

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
    private ProductStatus status = INSPECTING;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Builder
    public Product(Long id, User seller, String name, String description, String image_url,
        ProductSize size, ProductCategory category, String brand, Long startPrice,
        LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.seller = seller;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.size = size;
        this.category = category;
        this.brand = brand;
        this.startPrice = startPrice;
        this.startTime = startTime;
        this.endTime = endTime;


    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
