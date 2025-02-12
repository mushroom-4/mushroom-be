package nbc.mushroom.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.product.dto.response.searchProductRes;
import nbc.mushroom.domain.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {

    private final ProductService productService;

    // 상품 상세 조회
    @GetMapping("/{productId}/info")
    public ResponseEntity<ApiResponse<searchProductRes>> searchProduct(
        @PathVariable long productId) {
        searchProductRes searchProductRes = productService.searchProductRes(productId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success("상품이 정상적으로 조회되었습니다.", searchProductRes));
    }

}
