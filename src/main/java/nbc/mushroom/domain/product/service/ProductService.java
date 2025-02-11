package nbc.mushroom.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
}
