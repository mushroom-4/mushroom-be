package nbc.mushroom.domain.product.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.product.entity.ProductStatus;
import nbc.mushroom.domain.product.repository.ProductRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductStatusService {

    private final ProductRepository productRepository;

    @Scheduled(cron = "0 */5 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void startAuctions() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림

        List<Product> waitingProducts = productRepository.findProductByStatusAndStartTime(
            ProductStatus.WAITING, now);

        for (Product product : waitingProducts) {
            product.updateStatus(ProductStatus.PROGRESSING);
        }
    }
}
