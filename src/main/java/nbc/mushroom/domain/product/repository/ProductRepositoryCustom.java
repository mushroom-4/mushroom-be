package nbc.mushroom.domain.product.repository;

import nbc.mushroom.domain.product.entity.Product;

public interface ProductRepositoryCustom {

    Product findProductById(Long id);
}
