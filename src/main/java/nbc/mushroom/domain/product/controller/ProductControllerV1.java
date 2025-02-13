package nbc.mushroom.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.product.dto.request.CreateProductReq;
import nbc.mushroom.domain.product.dto.response.ProductRes;
import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import nbc.mushroom.domain.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {

    private final ProductService productService;

    // 상품 상세 조회
    @GetMapping("/{productId}/info")
    public ResponseEntity<ApiResponse<SearchProductRes>> searchProduct(
        @PathVariable long productId) {
        SearchProductRes searchProductRes = productService.searchProduct(productId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success("상품이 정상적으로 조회되었습니다.", searchProductRes));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductRes>> postProduct(
        @ModelAttribute CreateProductReq createProductReq,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.createProduct(userId, createProductReq));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductRes>> putProduct(
        @ModelAttribute CreateProductReq createProductReq,
        @PathVariable("productId") Long productId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();

        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.updateProduct(userId, productId, createProductReq));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
        @PathVariable("productId") Long productId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(productService.solfDeleteProduct(userId, productId));
    }

}
