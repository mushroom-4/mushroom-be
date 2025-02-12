package nbc.mushroom.domain.product.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.product.dto.request.CreateProductReq;
import nbc.mushroom.domain.product.dto.response.CreateProductRes;
import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.entity.ProductCategory;
import nbc.mushroom.domain.product.entity.ProductSize;
import nbc.mushroom.domain.product.repository.ProductRepository;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public SearchProductRes searchProduct(long productId) {
        Product searchProduct = productRepository.findProductById(productId);
        return SearchProductRes.from(searchProduct);
    }

    private final UserRepository userRepository;

    @Transactional
    public ApiResponse<CreateProductRes> createProduct(Long userId, CreateProductReq req,
        String imageUrl) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Product product = Product.builder()
            .seller(user)
            .name(req.name())
            .description(req.description())
            .brand(req.brand())
            .image_url(imageUrl)
            .size(ProductSize.valueOf(req.productSize()))
            .category(ProductCategory.valueOf(req.productCategory()))
            .startPrice(req.startPrice())
            .startTime(req.startTime())
            .endTime(req.endTime())
            .build();

        productRepository.save(product);
        return ApiResponse.success("상품 등록에 성공했습니다.", CreateProductRes.from(product));
    }
}
