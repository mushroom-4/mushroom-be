package nbc.mushroom.domain.bid.service;


import static nbc.mushroom.domain.common.exception.ExceptionType.AUCTION_ITEM_NOT_IN_PROGRESS;
import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_BIDDING_PRICE;
import static nbc.mushroom.domain.common.exception.ExceptionType.SELF_BIDDING_NOT_ALLOWED;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.dto.request.CreateBidReq;
import nbc.mushroom.domain.bid.dto.response.CreateBidRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.chat.service.ChatService;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final ChatService chatService;

    @Transactional(readOnly = false)
    public CreateBidRes createOrUpdateBid(
        User loginUser,
        Long auctionItemId,
        CreateBidReq createBidReq
    ) {

        AuctionItem findAuctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        validateBidRequest(loginUser, findAuctionItem, createBidReq.biddingPrice());

        Bid findBid = bidRepository.findBidByUserAndAuctionItem(loginUser, findAuctionItem)
            .orElseGet(() -> createBid(loginUser, findAuctionItem, createBidReq.biddingPrice())
            ); // 좀 더 생각.. Bid 생성까지 Repository에서 처리하는건 아닌듯..

        if (!createBidReq.biddingPrice().equals(findBid.getBiddingPrice())) {
            findBid.updateBiddingPrice(createBidReq.biddingPrice());
        }

        chatService.sendBidAnnouncementMessage(findAuctionItem.getId(), findBid.getBidder(),
            findBid.getBiddingPrice());

        return CreateBidRes.from(findBid);
    }

    public Boolean hasBid(Long bidderId, Long auctionItemId) {
        return bidRepository.existBidByBidderIdAndAuctionItemId(bidderId, auctionItemId);
    }

    private Bid createBid(User bidder, AuctionItem auctionItem, Long biddingPrice) {
        Bid bid = Bid.builder()
            .auctionItem(auctionItem)
            .biddingPrice(biddingPrice)
            .bidder(bidder)
            .build();

        return bidRepository.save(bid);
    }

    private void validateBidRequest(User bidder, AuctionItem auctionItem, Long biddingPrice) {
        if (auctionItem.getStatus() != AuctionItemStatus.PROGRESSING) {
            throw new CustomException(AUCTION_ITEM_NOT_IN_PROGRESS);
        }

        log.info("bidder id : {}", bidder.getId());
        log.info("seller id : {}", auctionItem.getSeller().getId());
        if (Objects.equals(bidder.getId(), auctionItem.getSeller().getId())) {
            throw new CustomException(SELF_BIDDING_NOT_ALLOWED);
        }

        if (auctionItem.getStartPrice() > biddingPrice) {
            throw new CustomException(INVALID_BIDDING_PRICE);
        }

        //  경매물품 Bid의 최고가 반환, 조회되는 bid 데이터가 없으면 acutionItem을 최고가로 설정
        Long highestBiddingPrice = Optional.ofNullable(
                bidRepository.findPotentiallySucceededBidByAuctionItem(auctionItem))
            .map(Bid::getBiddingPrice)
            .orElse(auctionItem.getStartPrice());

        if (highestBiddingPrice >= biddingPrice) {
            throw new CustomException(INVALID_BIDDING_PRICE);
        }
    }
}
