package nbc.mushroom.domain.auction_item.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            auctionItem.complete();

            Bid succedBid = bidRepository.findPotentiallySucceededBidByAuctionItem(auctionItem);
            succedBid.succeed();

            List<Bid> failedBids = bidRepository.findPotentiallyFailedBidsByAuctionItem(
                auctionItem);

            for (Bid bid : failedBids) {
                bid.fail();
            }
        }
    }
}
