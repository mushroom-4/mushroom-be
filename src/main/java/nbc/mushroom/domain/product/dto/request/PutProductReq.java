package nbc.mushroom.domain.product.dto.request;


import java.time.LocalDateTime;
import java.util.Optional;
import nbc.mushroom.domain.product.entity.ProductCategory;
import nbc.mushroom.domain.product.entity.ProductSize;
import org.springframework.web.multipart.MultipartFile;

public record PutProductReq(
    Optional<String> name,
    Optional<String> description,
    Optional<ProductSize> productSize,
    Optional<ProductCategory> productCategory,
    Optional<String> brand,
    Optional<Long> startPrice,
    Optional<LocalDateTime> startTime,
    Optional<LocalDateTime> endTime,
    Optional<MultipartFile> image
) {

}