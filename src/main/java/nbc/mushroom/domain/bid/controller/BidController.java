package nbc.mushroom.domain.bid.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.dto.response.BidRes;
import nbc.mushroom.domain.bid.service.BidService;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BidRes>>> getAllBids(
        @Auth AuthUser authUser,
        @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        User loginUser = User.fromAuthUser(authUser);

        Page<BidRes> getUserBidRes = bidService.getAllBidsByUser(loginUser, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("입찰 내역 목록 조회에 성공했습니다", getUserBidRes));
    }

    @GetMapping("/{bidId}")
    public ResponseEntity<ApiResponse<BidRes>> getBid(
        @Auth AuthUser authUser,
        @PathVariable Long bidId
    ) {
        User loginUser = User.fromAuthUser(authUser);
        BidRes bidRes = bidService.getBidByUser(loginUser, bidId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("입찰 내역 상세 조회에 성공했습니다.", bidRes));
    }

    @DeleteMapping("/{bidId}/cancel")
    public ResponseEntity<ApiResponse<Void>> deleteBid(
        @Auth AuthUser authUser,
        @PathVariable Long bidId
    ) {
        bidService.deleteBid(User.fromAuthUser(authUser), bidId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success("입찰 취소에 성공했습니다."));
    }
}
