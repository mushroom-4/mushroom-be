package nbc.mushroom.domain.product.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import nbc.mushroom.domain.product.entity.ProductCategory;
import nbc.mushroom.domain.product.entity.ProductSize;
import org.springframework.web.multipart.MultipartFile;

public record PutProductReq(
    @NotBlank(message = "상품 명은 필수이며, 공백일 수 없습니다.")
    String name,

    String description,

    @NotNull(message = "상품 사이즈는 필수입니다.")
    ProductSize productSize,
    @NotNull(message = "상품 카테고리는 필수입니다.")
    ProductCategory productCategory,
    @NotBlank(message = "상품 브랜드 명은 필수이며, 공백일 수 없습니다.")
    String brand,
    @NotNull(message = "상품의 입찰 시작 금액은 필수입니다.")
    Long startPrice,
    @NotNull(message = "상품의 입찰 시작 시간 설정은 필수입니다.")
    LocalDateTime startTime,
    @NotNull(message = "상품의 입찰 종료 시간 설정은 필수입니다.")
    LocalDateTime endTime,

    MultipartFile image
) {

}