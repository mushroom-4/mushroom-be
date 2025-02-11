package nbc.mushroom.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.product.dto.response.searchProductRes;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public searchProductRes searchProductRes(long productId) {
        Product searchProduct = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ExceptionType.PRODUCT_NOT_FOUND));
        return searchProductRes.from(searchProduct);
    }

}
