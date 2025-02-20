package nbc.mushroom.domain.admin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.admin.dto.response.AuctionItemStatusRes;
import nbc.mushroom.domain.admin.service.AuctionItemAdminService;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.common.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/auction-items")
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

    // 관리자 경매 물품 상태 목록 전체 조회 + 필터링 조회 기능 API
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuctionItemStatusRes>>> adminSearchAuctionItemsStatus(
        @RequestParam(required = false) List<AuctionItemStatus> status,
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<AuctionItemStatusRes> auctionItemsStatusRes = auctionItemAdminService
            .getAuctionItemsStatus(status, pageable);

        return ResponseEntity.ok(
            ApiResponse.success("경매 물품 상태 목록을 조회했습니다.", auctionItemsStatusRes));
    }
}
