package nbc.mushroom.domain.product.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.PRODUCT_NOT_USER;
import static nbc.mushroom.domain.common.exception.ExceptionType.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.util.image.ImageUtil;
import nbc.mushroom.domain.product.dto.request.CreateProductReq;
import nbc.mushroom.domain.product.dto.request.PutProductReq;
import nbc.mushroom.domain.product.dto.response.ProductRes;
import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.repository.ProductRepository;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ImageUtil imageUtil;

    public SearchProductRes searchProduct(long productId) {
        Product searchProduct = productRepository.findProductById(productId);
        return SearchProductRes.from(searchProduct);
    }


    @Transactional
    public ProductRes createProduct(Long userId,
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
            .size(createProductReq.productSize())
            .category(createProductReq.productCategory())
            .startPrice(createProductReq.startPrice())
            .startTime(createProductReq.startTime())
            .endTime(createProductReq.endTime())
            .build();

        productRepository.save(product);

        return ProductRes.from(product, imageUrl);
    }

    @Transactional
    public ProductRes updateProduct(Long userId, Long productId,
        PutProductReq putProductReq) {

        Product product = validateProdById(userId, productId);

        productRepository.findProductById(productId);

        User user = validateUserById(userId);

        String updateFileName = "";
        String updateImageUrl = "";

        if (!(putProductReq.image().isEmpty())) {
            imageUtil.delete(product.getImage_url());
            updateFileName = imageUtil.upload(
                putProductReq.image().get());
            updateImageUrl = imageUtil.getImageUrl(updateFileName);
        }
        if (putProductReq.image().isEmpty()) {
            imageUtil.delete(product.getImage_url());
            updateFileName = product.getImage_url();
            updateImageUrl = imageUtil.getImageUrl(updateFileName);
        }

        Product updateProduct = Product.builder()
            .id(productId)
            .seller(user)
            .name(putProductReq.name().orElse(product.getName()))
            .description(putProductReq.description().orElse(product.getDescription()))
            .brand(putProductReq.brand().orElse(product.getBrand()))
            .image_url(updateFileName)
            .size(putProductReq.productSize().orElse(product.getSize()))
            .category(putProductReq.productCategory().orElse(product.getCategory()))
            .startPrice(putProductReq.startPrice().orElse(product.getStartPrice()))
            .startTime(putProductReq.startTime().orElse(product.getStartTime()))
            .endTime(putProductReq.endTime().orElse(product.getEndTime()))
            .build();

        productRepository.save(updateProduct);

        return ProductRes.from(updateProduct, updateImageUrl);
    }

    @Transactional
    public void softDeleteProduct(Long userId, Long productId) {

        Product product = validateProdById(userId, productId);

        validateUserById(userId);

        product.softDelete();
    }

    private Product validateProdById(Long userId, Long productId) {
        Product product = productRepository.findProductById(productId);
        if (!product.getSeller().getId().equals(userId)) {
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
