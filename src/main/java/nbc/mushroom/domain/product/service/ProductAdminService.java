package nbc.mushroom.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.entity.ProductStatus;
import nbc.mushroom.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductAdminService {

    private final ProductRepository productRepository;

    // 물품 검수 합격 -> status 대기중 (waiting)
    @Transactional
    public void approveProduct(Long productId) {

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ExceptionType.PRODUCT_NOT_FOUND));

        validateStatusChange(product.getStatus(), ProductStatus.WAITING);
        product.updateStatus(ProductStatus.WAITING);
    }

    /**
     * 상태 변경 검증 상품 상태는 INSPECTING -> WAITING, INSPECTING -> REJECTED 로만 변경이 가능
     *
     * @param currentStatus 현재 상태
     * @param newStatus     변경할 상태
     */
    public void validateStatusChange(ProductStatus currentStatus, ProductStatus newStatus) {
        if (currentStatus == ProductStatus.INSPECTING && newStatus == ProductStatus.WAITING
            || newStatus == ProductStatus.REJECTED) {
            return;
        }
        // 상품의 상태가 inspecting이 아닌 경우 이미 검수가 완료된 상품이라고 가정
        if (currentStatus != ProductStatus.INSPECTING) {
            throw new CustomException(ExceptionType.PRODUCT_ALREADY_INSPECTED);
        }

        throw new CustomException(ExceptionType.INVALID_PRODUCT_STATUS);
    }

    // 물품 검수 불합격 -> status 실패 (rejected)
}
