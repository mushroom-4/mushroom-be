package nbc.mushroom.domain.bid.repository;

import java.util.List;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.entity.BiddingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid, Long>, BidRepositoryCustom {

    List<Bid> findBidsByAuctionItemAndBiddingStatus(
        AuctionItem auctionItem,
        BiddingStatus biddingStatus
    );
}
