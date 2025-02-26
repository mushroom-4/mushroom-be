package nbc.mushroom.domain.bid.service;

import static nbc.mushroom.domain.bid.entity.BiddingStatus.BIDDING;
import static nbc.mushroom.domain.bid.entity.BiddingStatus.CANCELED;
import static nbc.mushroom.domain.common.exception.ExceptionType.BID_CANCELLATION_LIMIT_EXCEEDED;
import static nbc.mushroom.domain.common.exception.ExceptionType.BID_CANNOT_CANCEL_NON_BIDDING;
import static nbc.mushroom.domain.common.exception.ExceptionType.BID_CANNOT_CANCEL_WITHIN_24HOURS;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.bid.dto.response.BidRes;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {

    private final BidRepository bidRepository;

    public Page<BidRes> getAllBidsByUser(User loginUser, Pageable pageable) {
        Page<Bid> bidPage = bidRepository.findBidsByUser(loginUser, pageable);

        return bidPage.map(bid ->
            BidRes.from(bid, SearchAuctionItemRes.from(bid.getAuctionItem())));
    }

    public BidRes getBidByUser(User loginUser, Long bidId) {
        Bid findBid = bidRepository.findBidByBidderAndId(loginUser, bidId);
        SearchAuctionItemRes searchAuctionItemRes = SearchAuctionItemRes.from(
            findBid.getAuctionItem());

        return BidRes.from(findBid, searchAuctionItemRes);
    }

    @Transactional(readOnly = false)
    public void deleteBid(User loginUser, Long bidId) {
        Bid findBid = bidRepository.findBidByBidderAndId(loginUser, bidId);

        validateBidCancellation(loginUser, findBid);

        findBid.cancel();
    }

    private void validateBidCancellation(User loginUser, Bid bid) {
        LocalDateTime now = LocalDateTime.now();

        if (bid.getBiddingStatus() != BIDDING) {
            throw new CustomException(BID_CANNOT_CANCEL_NON_BIDDING);
        }

        if (now.isAfter(bid.getAuctionItem().getEndTime().minusHours(24))) {
            throw new CustomException(BID_CANNOT_CANCEL_WITHIN_24HOURS);
        }

        if (bidRepository.countBidsByBidderAndStatus(loginUser, CANCELED) > 3) {
            throw new CustomException(BID_CANCELLATION_LIMIT_EXCEEDED);
        }
    }
}
