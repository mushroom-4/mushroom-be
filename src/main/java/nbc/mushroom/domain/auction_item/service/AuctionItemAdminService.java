package nbc.mushroom.domain.auction_item.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionItemAdminService {

    private final AuctionItemRepository auctionItemRepository;

    // 경매 물품 검수 합격 -> status 대기중 (waiting)
    @Transactional
    public void approveAuctionItem(Long auctionItemId) {

        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        auctionItem.approve();
    }

    // 경매 물품 검수 불합격 -> status 실패 (rejected)
    @Transactional
    public void rejectAuctionItem(Long auctionItemId) {

        AuctionItem auctionItem = auctionItemRepository.findAuctionItemById(auctionItemId);

        auctionItem.reject();
    }
}
