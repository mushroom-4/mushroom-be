package nbc.mushroom.domain.bid.service;


import static nbc.mushroom.domain.common.exception.ExceptionType.INVALID_BIDDING_PRICE;
import static nbc.mushroom.domain.common.exception.ExceptionType.SELF_BIDDING_NOT_ALLOWED;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
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
public class CreateBidService {

    private final BidRepository bidRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final ChatService chatService;

    @Transactional(readOnly = false)
    public CreateBidRes createOrUpdateBid(
        User bidder,
        Long auctionItemId,
        CreateBidReq createBidReq
    ) {
        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId)
            .throwIfNotInProgress();
        validateBidder(bidder, auctionItem);

        Optional<Bid> maxBid = bidRepository.findMaxPriceBidInAuctionItem(auctionItem);
        validateBidReq(createBidReq, maxBid, auctionItem);

        Bid prevBid = bidRepository.findBidByUserAndAuctionItem(bidder, auctionItem)
            .orElseGet(() -> createBid(bidder, auctionItem, createBidReq.biddingPrice()));
        if (!createBidReq.biddingPrice().equals(prevBid.getBiddingPrice())) {
            prevBid.updateBiddingPrice(createBidReq.biddingPrice());
        }

        chatService.sendBidAnnouncementMessage(auctionItem.getId(), prevBid.getBidder(),
            prevBid.getBiddingPrice());

        return CreateBidRes.from(prevBid);
    }

    private Bid createBid(User bidder, AuctionItem auctionItem, Long biddingPrice) {
        Bid bid = Bid.builder()
            .auctionItem(auctionItem)
            .biddingPrice(biddingPrice)
            .bidder(bidder)
            .build();

        return bidRepository.save(bid);
    }

    private void validateBidder(User bidder, AuctionItem auctionItem) {
        if (Objects.equals(bidder.getId(), auctionItem.getSeller().getId())) {
            throw new CustomException(SELF_BIDDING_NOT_ALLOWED);
        }
    }

    private void validateBidReq(CreateBidReq bidReq, Optional<Bid> maxBid,
        AuctionItem auctionItem) {
        //  경매물품 입찰내역중 최고가, 조회되는 bid 데이터가 없으면 시작가를 최고가로 설정
        Long highestBiddingPrice = maxBid
            .map(Bid::getBiddingPrice)
            .orElse(auctionItem.getStartPrice());

        if (highestBiddingPrice >= bidReq.biddingPrice()) {
            throw new CustomException(INVALID_BIDDING_PRICE);
        }
    }
}
