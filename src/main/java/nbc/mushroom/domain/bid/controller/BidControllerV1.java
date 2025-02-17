package nbc.mushroom.domain.bid.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.dto.request.CreateBidReq;
import nbc.mushroom.domain.bid.dto.response.CreateBidRes;
import nbc.mushroom.domain.bid.service.BidService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction-items/{auctionItemId}/bids")
public class BidControllerV1 {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateBidRes>> createOrUpdateBid(
        @Auth AuthUser authUser,
        @PathVariable Long auctionItemId,
        @Valid @RequestBody CreateBidReq createBidReq
    ) {
        CreateBidRes createBidRes = bidService.createOrUpdateBid(
            User.fromAuthUser(authUser),
            auctionItemId,
            createBidReq);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("입찰에 성공했습니다.", createBidRes));
    }
}
