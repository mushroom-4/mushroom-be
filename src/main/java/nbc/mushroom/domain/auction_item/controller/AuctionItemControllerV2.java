package nbc.mushroom.domain.auction_item.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemBidRes;
import nbc.mushroom.domain.auction_item.service.AuctionItemService;
import nbc.mushroom.domain.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auction-items")
public class AuctionItemControllerV2 {

    private final AuctionItemService auctionItemService;

    // 경매 물품 상세 조회
    @GetMapping("/{auctionItemId}/info")
    public ResponseEntity<ApiResponse<SearchAuctionItemBidRes>> searchAuctionItem(
        @PathVariable long auctionItemId) {
        SearchAuctionItemBidRes searchAuctionItemBidRes = auctionItemService.searchAuctionItemV2(
            auctionItemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success("경매 물품이 정상적으로 조회되었습니다.", searchAuctionItemBidRes));
    }

}
