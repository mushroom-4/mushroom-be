package nbc.mushroom.domain.bid.repository;

import java.util.Optional;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.product.entity.Product;
import nbc.mushroom.domain.user.entity.User;

public interface BidRepositoryCustom {
    
    Optional<Bid> findBidByUserAndProduct(User bidder, Product product);
}
