package nbc.mushroom.domain.auction_item.repository;

import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long>,
    AuctionItemRepositoryCustom {

}
