package nbc.mushroom.domain.bid.repository;

import java.util.List;
import java.util.Optional;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemBidInfoRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import nbc.mushroom.domain.notice.dto.SearchNoticeEndTypeRes;
import nbc.mushroom.domain.notice.dto.SearchNoticeRes;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BidRepositoryCustom {

    Optional<Bid> findBidByUserAndAuctionItem(User bidder, AuctionItem auctionItem);

    Bid findPotentiallySucceededBidByAuctionItem(AuctionItem auctionItem);

    List<Bid> findPotentiallyFailedBidsByAuctionItem(AuctionItem auctionItem);

    Boolean existsBidByAuctionItem(AuctionItem auctionItem);

    Page<Bid> findBidsByUser(User user, Pageable pageable);

    Bid findBidByBidderAndId(User bidder, Long bidId);

    Long countBidsByBidderAndStatus(User bidder, BiddingStatus biddingStatus);

    AuctionItemBidInfoRes auctionItemBidInfoFind(Long id);

    Boolean existBidByBidderIdAndAuctionItemId(Long bidderId, Long auctionItemId);

    List<SearchNoticeEndTypeRes> auctionItemBidInfoFindList(
        List<SearchNoticeRes> auctionItemIdByNoticeList);
}
