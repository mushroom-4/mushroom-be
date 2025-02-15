package nbc.mushroom.domain.auction_item.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionItemStatusService {

    private final AuctionItemRepository auctionItemRepository;
    private final BidRepository bidRepository;

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

    @Scheduled(cron = "0 */5 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void completeAuctions() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림

        List<AuctionItem> progressingAuctionItems = auctionItemRepository.findAuctionItemsByStatusAndEndTime(
            AuctionItemStatus.PROGRESSING, now);

        for (AuctionItem auctionItem : progressingAuctionItems) {
            log.info("auction ID : {}", auctionItem.getId().toString());
            auctionItem.complete();
            log.info("auction Status : {}", auctionItem.getStatus());

            if (Boolean.FALSE.equals(bidRepository.existsBidByAuctionItem(auctionItem))) {
                auctionItem.untrade();
                log.info("auction untrade id : {}", auctionItem.getId());
                log.info("auction untrade Status : {}", auctionItem.getStatus());
                continue;
            }

            Bid succedBid = bidRepository.findPotentiallySucceededBidByAuctionItem(auctionItem);
            log.info("succedBid ID : {}", succedBid.getId().toString());
            succedBid.succeed();

            // 최고가 아닌 Bid들을 fail 처리
            bidRepository.findPotentiallyFailedBidsByAuctionItem(auctionItem)
                .forEach(Bid::fail);
        }
    }
}
