package nbc.mushroom.domain.admin.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.admin.dto.response.AuctionItemStatusRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItem;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.auction_item.repository.AuctionItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    // 관리자 경매 물품 상태 목록 전체 조회 + 상태별 필터링 조회
    @Transactional(readOnly = true)
    public Page<AuctionItemStatusRes> getAuctionItemsStatus(
        List<AuctionItemStatus> status,
        Pageable pageable
    ) {
        return auctionItemRepository.findAuctionItemsByStatus(status, pageable);
    }
}
