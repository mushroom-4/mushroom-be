package nbc.mushroom.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.product.dto.request.CreateProductReq;
import nbc.mushroom.domain.product.dto.response.CreateProductRes;
import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import nbc.mushroom.domain.product.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {

    private final ProductService productService;
    private final ImageUtil imageUtil;

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
    public ResponseEntity<ApiResponse<CreateProductRes>> PostProduct(
        @ModelAttribute CreateProductReq createProductReq,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        String filename = imageUtil.upload(createProductReq.image());
        String imageUrl = imageUtil.getImageUrl(filename);
        return ResponseEntity.ok()
            .body(productService.createProduct(userId, createProductReq, imageUrl));
    }

    // 상품 전체 조회 (페이징 조회 포함)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchProductRes>>> searchAllProducts(
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<SearchProductRes> findAllProducts = productService.findAllProducts(pageable);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success("상품이 전제 조회 되었습니다.", findAllProducts));
    }
}
