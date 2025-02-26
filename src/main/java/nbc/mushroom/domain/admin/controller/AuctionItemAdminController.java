package nbc.mushroom.domain.admin.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.admin.dto.request.UpdateAuctionItemStatusReq;
import nbc.mushroom.domain.admin.dto.response.AuctionItemStatusRes;
import nbc.mushroom.domain.admin.service.AuctionItemAdminService;
import nbc.mushroom.domain.auction_item.entity.AuctionItemStatus;
import nbc.mushroom.domain.common.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/auction-items")
@RequiredArgsConstructor
public class AuctionItemAdminController {

    private final AuctionItemAdminService auctionItemAdminService;

    @PatchMapping("/{auctionItemId}")
    public ResponseEntity<ApiResponse<Void>> updateAuctionItemStatus(
        @PathVariable Long auctionItemId,
        @Valid @RequestBody UpdateAuctionItemStatusReq updateAuctionItemStatusReq
    ) {
        auctionItemAdminService.updateAuctionItemStatus(auctionItemId, updateAuctionItemStatusReq);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("물품 검수가 완료되었습니다."));
    }

    // 관리자 경매 물품 상태 목록 전체 조회 + 필터링 조회 기능 API
    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuctionItemStatusRes>>> getFilteredAuctionItemsByStatus(
        @RequestParam(value = "status", required = false) List<AuctionItemStatus> statusList,
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<AuctionItemStatusRes> auctionItemsStatusRes = auctionItemAdminService
            .getFilteredAuctionItemsByStatus(statusList, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("경매 물품 상태 목록을 조회했습니다.", auctionItemsStatusRes));
    }
}
