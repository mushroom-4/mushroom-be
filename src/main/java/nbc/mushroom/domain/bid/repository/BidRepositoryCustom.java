package nbc.mushroom.domain.bid.repository;

import java.util.List;
import java.util.Optional;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.user.entity.User;

public interface BidRepositoryCustom {

    Optional<Bid> findBidByUserAndAuctionItem(User bidder, AuctionItem auctionItem);

    Bid findPotentiallySucceededBidByAuctionItem(AuctionItem auctionItem);

    List<Bid> findPotentiallyFailedBidsByAuctionItem(AuctionItem auctionItem);

    Boolean existsBidByAuctionItem(AuctionItem auctionItem);
}
