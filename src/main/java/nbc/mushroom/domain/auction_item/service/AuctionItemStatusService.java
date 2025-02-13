package nbc.mushroom.domain.auction_item.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionItemStatusService {

    private final AuctionItemRepository auctionItemRepository;

    @Scheduled(cron = "0 */5 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void startAuctions() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림

        List<AuctionItem> waitingAuctionItems = auctionItemRepository.findAuctionItemsByStatusAndStartTime(
            AuctionItemStatus.WAITING, now);

        for (AuctionItem auctionItem : waitingAuctionItems) {
            auctionItem.start();
        }
    }
}
