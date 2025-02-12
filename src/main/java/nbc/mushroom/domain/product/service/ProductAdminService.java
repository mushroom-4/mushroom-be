package nbc.mushroom.domain.product.service;

import lombok.RequiredArgsConstructor;
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

        Product product = productRepository.findProductById(productId);

        product.updateStatus(ProductStatus.WAITING);
    }

    // 물품 검수 불합격 -> status 실패 (rejected)
    @Transactional
    public void rejectProduct(Long productId) {

        Product product = productRepository.findProductById(productId);

        product.updateStatus(ProductStatus.REJECTED);
    }
}
