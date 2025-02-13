package nbc.mushroom.domain.product.repository;

import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import java.time.LocalDateTime;
import java.util.List;
import nbc.mushroom.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import nbc.mushroom.domain.product.entity.ProductStatus;

public interface ProductRepositoryCustom {

    Product findProductById(Long id);

    Page<SearchProductRes> findAllProducts(Pageable pageable);

    List<Product> findProductByStatusAndStartTime(ProductStatus productStatus, LocalDateTime now);
}
