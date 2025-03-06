package nbc.mushroom.domain.auction_item.service;

import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.PROGRESSING;
import static nbc.mushroom.domain.auction_item.entity.AuctionItemStatus.WAITING;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.chat.service.ChatRoomService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuctionItemStatusService {

    private final AuctionItemRepository auctionItemRepository;
    private final BidRepository bidRepository;
    private final ChatRoomService chatRoomService;

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

            List<Bid> biddingBidsList = bidRepository.findBidsByAuctionItemAndBiddingStatus(
                auctionItem,
                BiddingStatus.BIDDING
            );

            // 최고 입찰 찾기
            Bid succeedBid = biddingBidsList.stream()
                .max(Comparator.comparing(Bid::getBiddingPrice))
                .orElse(null);

            // 최고 입찰이 없으면 non-trade
            if (succeedBid == null) {
                auctionItem.nonTrade();
                log.info("auction non-trade id : {}", auctionItem.getId());
                log.info("auction non-trade Status : {}", auctionItem.getStatus());
                continue;
            }

            // 최고 입찰은 성공으로
            succeedBid.succeed();

            // 나머지는 모두 실패로
            biddingBidsList.stream()
                .filter(bid -> !bid.equals(succeedBid))
                .forEach(Bid::fail);

            // 경매 물품 완료처리
            auctionItem.complete();
            log.info("auction ID : {}", auctionItem.getId().toString());
            log.info("auction Status : {}", auctionItem.getStatus());

            // 채팅방 메시지 기록 만료 시간 설정
            chatRoomService.setChatRoomStorageTTL(auctionItem.getId());

        }
    }
}
