package nbc.mushroom.domain.auction_item.repository;

import java.time.LocalDateTime;
import java.util.List;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuctionItemRepositoryCustom {

    AuctionItem findAuctionItemById(Long id);

    Page<SearchAuctionItemRes> findAllAuctionItems(Pageable pageable);

    List<AuctionItem> findAuctionItemsByStatusAndStartTime(AuctionItemStatus auctionItemStatus,
        LocalDateTime now);

    boolean existsByUserAndAuctionItem(User user, Long auctionItemId);
}
