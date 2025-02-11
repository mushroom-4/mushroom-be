package nbc.mushroom.domain.product.dto.response;

import java.time.LocalDateTime;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.entity.ProductCategory;
import nbc.mushroom.domain.product.entity.ProductSize;
import nbc.mushroom.domain.product.entity.ProductStatus;

public record searchProductRes(
    Long productId,
    String name,
    String description,
    String imageUrl,
    ProductSize size,
    ProductCategory category,
    String brand,
    Long startPrice,
    LocalDateTime startTime,
    LocalDateTime endTime,
    ProductStatus status
) {

    public static searchProductRes from(Product searchProduct) {
        return new searchProductRes(
            searchProduct.getId(),
            searchProduct.getName(),
            searchProduct.getDescription(),
            searchProduct.getImage_url(),
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
