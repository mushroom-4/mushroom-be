package nbc.mushroom.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auction_item.dto.response.AuctionItemRes;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.service.AuctionItemRegisterServiceV1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/registrations")
public class AuctionItemRegisterControllerV1 {

    private final AuctionItemRegisterServiceV1 auctionItemRegisterServiceV1;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AuctionItemRes>>> getRegisteredAuctionItems(
        @Auth AuthUser authUser,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "35") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<AuctionItemRes> auctionItems = auctionItemRegisterServiceV1.searchRegisteredAuctionItems(
            authUser.id(), pageable);

        return ResponseEntity.ok(ApiResponse.success("등록 경매 물품 목록 조회에 성공했습니다.", auctionItems));
    }
}
