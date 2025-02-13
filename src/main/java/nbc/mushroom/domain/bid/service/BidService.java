package nbc.mushroom.domain.bid.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import nbc.mushroom.domain.bid.dto.request.CreateBidReq;
import nbc.mushroom.domain.bid.dto.response.CreateBidRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.common.exception.ExceptionType;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionItemRepository auctionItemRepository;

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

        return CreateBidRes.from(findBid);
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
            throw new CustomException(ExceptionType.AUCTION_ITEM_NOT_IN_PROGRESS);
        }
        if (bidder == auctionItem.getSeller()) {
            throw new CustomException(ExceptionType.SELF_BIDDING_NOT_ALLOWED);
        }
        if (auctionItem.getStartPrice() > biddingPrice) {
            throw new CustomException(ExceptionType.INVALID_BIDDING_PRICE);
        }
    }
}
