package nbc.mushroom.domain.auction_item.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.service.AuctionItemAdminService;
import nbc.mushroom.domain.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auction-items/admin")
@RequiredArgsConstructor
public class AuctionItemAdminControllerV1 {

    private final AuctionItemAdminService auctionItemAdminService;

    // 물품 검수 합격 -> status 대기중 (waiting)
    @PatchMapping("/{auctionItemId}/approve")
    public ResponseEntity<ApiResponse<Void>> adminApproveAuctionItem(
        @PathVariable Long auctionItemId) {

        auctionItemAdminService.approveAuctionItem(auctionItemId);

        return ResponseEntity.ok(
            ApiResponse.success("관리자가 경매 물품을 승인했습니다.")
        );
    }

    @PatchMapping("/{auctionItemId}/reject")
    public ResponseEntity<ApiResponse<Void>> adminRejectAuctionItem(
        @PathVariable Long auctionItemId) {

        auctionItemAdminService.rejectAuctionItem(auctionItemId);

        return ResponseEntity.ok(
            ApiResponse.success("관리자가 경매 물품을 반려했습니다.")
        );
    }
}
