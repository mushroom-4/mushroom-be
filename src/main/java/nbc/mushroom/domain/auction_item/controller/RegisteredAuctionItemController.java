package nbc.mushroom.domain.auction_item.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.auction_item.service.RegisteredAuctionItemService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/registrations")
public class RegisteredAuctionItemController {

    private final RegisteredAuctionItemService registeredAuctionItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuctionItemRes>>> getAuctionItemsByUser(
        @Auth AuthUser authUser,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "35") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<AuctionItemRes> auctionItems = registeredAuctionItemService.getAuctionItemsByUser(
            authUser.id(), pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("등록 경매 물품 목록 조회에 성공했습니다.", auctionItems));
    }
}
