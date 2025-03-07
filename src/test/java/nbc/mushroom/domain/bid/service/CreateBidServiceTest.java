package nbc.mushroom.domain.bid.service;

import static nbc.mushroom.domain.user.entity.UserRole.USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.dto.request.CreateBidReq;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.chat.service.ChatService;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
class CreateBidServiceTest {

    @MockitoBean
    private ChatService chatService;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private CreateBidService createBidService;

    @Autowired
    private AuctionItemRepository auctionItemRepository;

    @Autowired
    private UserRepository userRepository;

    private User bidder1;
    private User bidder2;
    private AuctionItem auctionItem;

    @BeforeAll
    public void setup() {
        User seller = User.builder()
            .email("seller@seller.seller")
            .nickname("seller")
            .password("seller")
            .userRole(USER)
            .build();
        bidder1 = User.builder()
            .email("bidder1@bidder.bidder")
            .nickname("bidder1")
            .password("bidder1")
            .userRole(USER)
            .build();
        bidder2 = User.builder()
            .email("bidder2@bidder.bidder")
            .nickname("bidder2")
            .password("bidder2")
            .userRole(USER)
            .build();
        userRepository.saveAllAndFlush(List.of(seller, bidder1, bidder2));

        auctionItem = AuctionItem.builder()
            .seller(seller)
            .name("testItem")
            .size(AuctionItemSize.FREE)
            .category(AuctionItemCategory.ETC)
            .brand("testBrand")
            .startPrice(0L)
            .startTime(LocalDateTime.parse("2025-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2059-12-31T23:59:59"))
            .build();
        auctionItem.approve();
        auctionItem.start();
        auctionItemRepository.saveAndFlush(auctionItem);

        // chatService 함수 호출이 redis 에 접근하지 않도록 설정
        doNothing().when(chatService).sendBidAnnouncementMessage(any(), any(), any());
    }

    @AfterEach
    public void after() {
        bidRepository.deleteAll();
    }

    @Nested
    @DisplayName("입찰 기본 테스트")
    class NormalBiddingTest {

        @Test
        @DisplayName("경매 물품에 새로운 입찰이 하나 생성된다.")
        void insertOneBid() {
            //given
            CreateBidReq bidReq = new CreateBidReq(10_000L);

            //when
            createBidService.createOrUpdateBid(bidder1, auctionItem.getId(), bidReq);

            //then
            List<Bid> bidList = bidRepository
                .findBidsByAuctionItemAndBiddingStatus(auctionItem, BiddingStatus.BIDDING);

            assertThat(bidList).hasSize(1);
            assertThat(bidList.get(0).getBiddingPrice()).isEqualTo(10_000L);
        }

        @Test
        @DisplayName("경매 물품에 새로운 입찰이 두 개 생성된다.")
        void insertTwoBids() {
            //given
            CreateBidReq bidReq1 = new CreateBidReq(10_000L);
            CreateBidReq bidReq2 = new CreateBidReq(10_500L);

            //when
            createBidService.createOrUpdateBid(bidder1, auctionItem.getId(), bidReq1);
            createBidService.createOrUpdateBid(bidder2, auctionItem.getId(), bidReq2);

            //then
            List<Bid> bidList = bidRepository
                .findBidsByAuctionItemAndBiddingStatus(auctionItem, BiddingStatus.BIDDING);

            assertThat(bidList).hasSize(2);
            assertThat(bidList.get(0).getBiddingPrice()).isEqualTo(10_000L);
            assertThat(bidList.get(1).getBiddingPrice()).isEqualTo(10_500L);
        }
    }

    @Nested
    @DisplayName("입찰 동시성 테스트")
    class ConcurrencyBiddingTest {

        @Test
        @DisplayName("같은 금액의 입찰을 100개 동시에 요청할 때 하나만 입찰되어야 한다")
        void createBid100SamePrice() throws InterruptedException {
            //given
            int threadCount = 100;
            ExecutorService executorService = Executors.newFixedThreadPool(10);
            CountDownLatch latch = new CountDownLatch(threadCount);
            CreateBidReq bidReq = new CreateBidReq(10_500L);

            //when
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    try {
                        createBidService.createOrUpdateBid(bidder2, auctionItem.getId(), bidReq);
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();

            //then
            List<Bid> bidList = bidRepository
                .findBidsByAuctionItemAndBiddingStatus(auctionItem, BiddingStatus.BIDDING);
            assertThat(bidList).hasSize(1);
        }
    }
}
