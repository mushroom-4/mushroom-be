package nbc.mushroom.domain.auction_item.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nbc.mushroom.domain.auction_item.dto.request.CreateAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.request.PutAuctionItemReq;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.dto.response.SearchAuctionItemRes;
import nbc.mushroom.domain.auction_item.entity.AuctionItemCategory;
import nbc.mushroom.domain.auction_item.entity.AuctionItemSize;
import nbc.mushroom.domain.auction_item.service.AuctionItemService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction-items")
public class AuctionItemControllerV1 {

    private final AuctionItemService auctionItemService;

    // 경매 물품 생성
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

    // 경매 물품 키워드 조회
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<SearchAuctionItemRes>>> searchKeywordAuctionItems(
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "sort", defaultValue = "name") String sort,
        @RequestParam(value = "sortOrder", defaultValue = "ASC") String sortOrder,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "brand", required = false) String brand,
        @RequestParam(value = "category", required = false) AuctionItemCategory category,
        @RequestParam(value = "size", required = false) AuctionItemSize size,
        @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
        @RequestParam(value = "minPrice", required = false) Long minPrice,
        @RequestParam(value = "maxPrice", required = false) Long maxPrice) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sort, sortOrder));
        Page<SearchAuctionItemRes> searchKeywordAuctionItems = auctionItemService.searchKeywordAuctionItems(
            sort, sortOrder, keyword, brand, category, size, startDate, endDate, minPrice,
            maxPrice, pageable);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(ApiResponse.success(
                "해당 키워드를 가진 경매 물품들이 모두 조회되었습니다.",
                searchKeywordAuctionItems
            ));
    }

    // 경매 물품 수정
    @PutMapping(value = "/{auctionItemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<AuctionItemRes>> putAuctionItem(
        @Valid @ModelAttribute PutAuctionItemReq putAuctionItemReq,
        @PathVariable Long auctionItemId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        AuctionItemRes auctionItemRes = auctionItemService.updateAuctionItem(userId, auctionItemId,
            putAuctionItemReq);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("경매 물품 수정에 성공했습니다.", auctionItemRes));
    }

    // 경매 물품 삭제
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

    // 인기 검색어 조회
    @GetMapping("/popular-keywords")
    public ResponseEntity<ApiResponse<List<String>>> getPopularKeywords() {

        List<String> popularKeywords = auctionItemService.searchPopularKeywords();

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("인기 검색어 조회에 성공했습니다.", popularKeywords));
    }

    // 캐시 내용 가시화 API
    @GetMapping("/popular-keywords/cache")
    public ResponseEntity<ApiResponse<Void>> printCache() {
        auctionItemService.printPopularKeywordsCacheContents();
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("인기 검색어 캐시 내용이 로그에 출력되었습니다."));
    }
}
