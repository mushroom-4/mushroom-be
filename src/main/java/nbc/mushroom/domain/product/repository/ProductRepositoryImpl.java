package nbc.mushroom.domain.product.repository;

import static nbc.mushroom.domain.product.entity.QProduct.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.product.dto.response.SearchProductRes;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.entity.QProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final ProductRepository productRepository;


    @Override
    public Product findProductById(Long id) {
        return Optional.ofNullable(queryFactory
            .select(product)
            .from(product)
            .where(product.id.eq(id))
            .fetchOne()
        ).orElseThrow(() -> new CustomException(ExceptionType.PRODUCT_NOT_FOUND));
    }

    @Override
    public Page<SearchProductRes> findAllProducts(Pageable pageable) {
        QProduct product = QProduct.product;

        JPAQuery<SearchProductRes> query = queryFactory
            .select(Projections.constructor(SearchProductRes.class,
                product.id,
                product.name,
                product.description,
                product.image_url,
                product.size,
                product.category,
                product.brand,
                product.startPrice,
                product.startTime,
                product.endTime,
                product.status
            ))
            .from(product)
            .where(product.isDeleted.eq(false))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize());

        // 전체 데이터 수를 가져오기 위한 쿼리 (페이징을 위해 필요)
        JPAQuery<Long> countQuery = queryFactory.select(product.count())
            .from(product)
            .where(product.isDeleted.eq(false));

        // 쿼리 실행 후, 결과 데이터를 리스트로 반환
        List<SearchProductRes> content = query.fetch();

        // Page 객체를 만들어서 반환
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}