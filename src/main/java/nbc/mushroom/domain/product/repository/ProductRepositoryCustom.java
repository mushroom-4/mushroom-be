package nbc.mushroom.domain.product.repository;

import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import nbc.mushroom.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Product findProductById(Long id);

    Page<SearchProductRes> findAllProducts(Pageable pageable);
    
}
