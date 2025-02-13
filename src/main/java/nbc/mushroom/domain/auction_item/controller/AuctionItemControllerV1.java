package nbc.mushroom.domain.auction_item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.request.CreateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.request.PutAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.service.AuctionItemService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction-items")
public class AuctionItemControllerV1 {

    private final AuctionItemService auctionItemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AuctionItemRes>> postAuctionItem(
        @Valid @ModelAttribute CreateAuctionItemReq createAuctionItemReq,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        AuctionItemRes auctionItemRes = auctionItemService.createAuctionItem(userId,
            createAuctionItemReq);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("경매 물품 등록에 성공했습니다.", auctionItemRes));
    }

    // 경매 물품 전체 조회 (페이징 조회 포함)
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchAuctionItemRes>>> searchAllAuctionItems(
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<SearchAuctionItemRes> allAuctionItems = auctionItemService.findAllAuctionItems(
            pageable);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success("경매 물품이 전제 조회 되었습니다.", allAuctionItems));
    }

    // 경매 물품 상세 조회
    @GetMapping("/{auctionItemId}/info")
    public ResponseEntity<ApiResponse<SearchAuctionItemRes>> searchAuctionItem(
        @PathVariable long auctionItemId) {
        SearchAuctionItemRes searchAuctionItemRes = auctionItemService.searchAuctionItem(
            auctionItemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success("경매 물품이 정상적으로 조회되었습니다.", searchAuctionItemRes));
    }

    @PutMapping(value = "/{auctionItemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AuctionItemRes>> putAuctionItem(
        @ModelAttribute PutAuctionItemReq putAuctionItemReq,
        @PathVariable Long auctionItemId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        AuctionItemRes auctionItemRes = auctionItemService.updateAuctionItem(userId, auctionItemId,
            putAuctionItemReq);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("경매 물품 수정에 성공했습니다.", auctionItemRes));
    }

    @DeleteMapping(value = "/{auctionItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteAuctionItem(
        @PathVariable Long auctionItemId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();

        auctionItemService.softDeleteAuctionItem(userId, auctionItemId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success("경매 물품 삭제에 성공했습니다."));
    }
}
