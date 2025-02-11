package nbc.mushroom.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductAdminService {

    private final ProductRepository productRepository;
}
