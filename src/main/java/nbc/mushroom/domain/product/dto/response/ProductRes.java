package nbc.mushroom.domain.product.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.entity.ProductCategory;
import nbc.mushroom.domain.product.entity.ProductSize;
import nbc.mushroom.domain.product.entity.ProductStatus;

public record ProductRes(Long productId, String name,
                         String description, String image_url,
                         ProductSize size, ProductCategory category,
                         String brand, Long startPrice,
                         LocalDateTime startTime, LocalDateTime endTime,
                         ProductStatus productStatus
) {

    public static ProductRes from(Product searchProduct, String image_url) {
        return new ProductRes(
            searchProduct.getId(),
            searchProduct.getName(),
            searchProduct.getDescription(),
            image_url,
            searchProduct.getSize(),
            searchProduct.getCategory(),
            searchProduct.getBrand(),
            searchProduct.getStartPrice(),
            searchProduct.getStartTime(),
            searchProduct.getEndTime(),
            searchProduct.getStatus()
        );
    }
}
