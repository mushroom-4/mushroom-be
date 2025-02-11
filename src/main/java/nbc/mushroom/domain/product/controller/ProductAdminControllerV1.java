package nbc.mushroom.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.product.service.ProductAdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products/admin")
@RequiredArgsConstructor
public class ProductAdminControllerV1 {

    private final ProductAdminService productAdminService;
}
