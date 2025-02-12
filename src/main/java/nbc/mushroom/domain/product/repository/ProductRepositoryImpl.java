package nbc.mushroom.domain.product.repository;

import static nbc.mushroom.domain.product.entity.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.product.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Product findProductById(Long id) {
        return Optional.ofNullable(queryFactory
            .select(product)
            .from(product)
            .where(product.id.eq(id))
            .fetchOne()
        ).orElseThrow(() -> new CustomException(ExceptionType.PRODUCT_NOT_FOUND));
    }
}
