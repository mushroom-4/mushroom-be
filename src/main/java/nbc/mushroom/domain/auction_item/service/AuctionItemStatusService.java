package nbc.mushroom.domain.auction_item.service;

import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.PROGRESSING;
import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.WAITING;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
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

    @Scheduled(cron = "0 */1 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void startAuctions() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림
        log.info("startAuctions() start time: {}", now);

        List<AuctionItem> waitingAuctionItems = auctionItemRepository
            .findAuctionItemsByStatusAndStartTime(WAITING, now);
        log.info("auctionItem count : {}", waitingAuctionItems.size());

        for (AuctionItem auctionItem : waitingAuctionItems) {
            log.info("auctionItem ID : {}", auctionItem.getId());
            auctionItem.start();
            log.info("auctionItem status : {}", auctionItem.getStatus());
        }
    }

    @Scheduled(cron = "0 */1 * * * *") // 매 5분마다 (정각 기준)
    @Transactional(readOnly = false)
    public void completeAuctions() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES); // 초 단위 버림
        log.info("completeAuctions() start time: {}", now);

        List<AuctionItem> progressingAuctionItems = auctionItemRepository.findAuctionItemsByStatusAndEndTime(
            PROGRESSING, now);

        for (AuctionItem auctionItem : progressingAuctionItems) {
            log.info("auction endTime : {}", auctionItem.getEndTime());

            if (Boolean.FALSE.equals(bidRepository.existsBidByAuctionItem(auctionItem))) {
                auctionItem.nonTrade();
                log.info("auction non-trade id : {}", auctionItem.getId());
                log.info("auction non-trade Status : {}", auctionItem.getStatus());
                continue;
            }

            log.info("auction ID : {}", auctionItem.getId().toString());
            auctionItem.complete();
            log.info("auction Status : {}", auctionItem.getStatus());

            Bid succedBid = bidRepository.findPotentiallySucceededBidByAuctionItem(auctionItem);
            log.info("succeedBid ID : {}", succedBid.getId().toString());
            succedBid.succeed();

            // 최고가 아닌 Bid들을 fail 처리
            bidRepository.findPotentiallyFailedBidsByAuctionItem(auctionItem, succedBid)
                .forEach(Bid::fail);
        }
    }
}
