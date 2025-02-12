package nbc.mushroom.domain.product.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.PRODUCT_NOT_USER;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.product.dto.request.CreateProductReq;
import nbc.mushroom.domain.product.dto.response.ProductRes;
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
    private final UserRepository userRepository;
    private final ImageUtil imageUtil;

    public SearchProductRes searchProduct(long productId) {
        Product searchProduct = productRepository.findProductById(productId);
        return SearchProductRes.from(searchProduct);
    }


    @Transactional
    public ApiResponse<ProductRes> createProduct(Long userId,
        CreateProductReq createProductReq) {
        User user = validateUserById(userId);

        String fileName = imageUtil.upload(createProductReq.image());
        String imageUrl = imageUtil.getImageUrl(fileName);

        Product product = Product.builder()
            .seller(user)
            .name(createProductReq.name())
            .description(createProductReq.description())
            .brand(createProductReq.brand())
            .image_url(fileName)
            .size(ProductSize.valueOf(createProductReq.productSize()))
            .category(ProductCategory.valueOf(createProductReq.productCategory()))
            .startPrice(createProductReq.startPrice())
            .startTime(createProductReq.startTime())
            .endTime(createProductReq.endTime())
            .build();

        productRepository.save(product);
        return ApiResponse.success("상품 등록에 성공했습니다.", ProductRes.from(product, imageUrl));
    }

    @Transactional
    public ApiResponse<ProductRes> updateProduct(Long userId, Long productId,
        CreateProductReq createProductReq) {

        Product product = validateProdById(userId, productId);

        User user = validateUserById(userId);

        imageUtil.delete(product.getImage_url());

        String newFileName = imageUtil.upload(createProductReq.image());
        String newImageUrl = imageUtil.getImageUrl(newFileName);

        Product updateProduct = Product.builder()
            .id(productId)
            .seller(user)
            .name(createProductReq.name())
            .description(createProductReq.description())
            .brand(createProductReq.brand())
            .image_url(newFileName)
            .size(ProductSize.valueOf(createProductReq.productSize()))
            .category(ProductCategory.valueOf(createProductReq.productCategory()))
            .startPrice(createProductReq.startPrice())
            .startTime(createProductReq.startTime())
            .endTime(createProductReq.endTime())
            .build();

        productRepository.save(updateProduct);

        return ApiResponse.success("상품 수정에 성공했습니다.",
            ProductRes.from(updateProduct, newImageUrl));
    }

    @Transactional
    public ApiResponse<Void> solfDeleteProduct(Long userId, Long productId) {

        Product product = validateProdById(userId, productId);

        validateUserById(userId);

        product.softDelete();

        return ApiResponse.success("상품 삭제에 성공했습니다.");
    }

    private Product validateProdById(Long userId, Long productId) {
        Product product = productRepository.findProductById(productId);
        if (product.getSeller().getId() != userId) {
            throw new CustomException(PRODUCT_NOT_USER);
        }
        return product;
    }

    private User validateUserById(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        return user;
    }
}
