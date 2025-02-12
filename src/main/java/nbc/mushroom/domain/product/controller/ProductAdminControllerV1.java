package nbc.mushroom.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.product.service.ProductAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products/admin")
@RequiredArgsConstructor
public class ProductAdminControllerV1 {

    private final ProductAdminService productAdminService;

    // 물품 검수 합격 -> status 대기중 (waiting)
    @PatchMapping("/{product_id}/approve")
    public ResponseEntity<ApiResponse<Void>> adminApproveProduct(
        @PathVariable("product_id") Long productId) {

        productAdminService.approveProduct(productId);

        return ResponseEntity.ok(
            ApiResponse.success("관리자가 상품을 승인했습니다.")
        );
    }

    @PatchMapping("/{product_id}/reject")
    public ResponseEntity<ApiResponse<Void>> adminRejectProduct(
        @PathVariable("product_id") Long productId) {

        productAdminService.rejectProduct(productId);

        return ResponseEntity.ok(
            ApiResponse.success("관리자가 상품을 반려했습니다.")
        );
    }

}
