package nbc.mushroom.domain.like.controller;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.like.service.AuctionItemLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auction-items")
public class AuctionItemLikeController {

    private final AuctionItemLikeService auctionItemLikeService;

    @PostMapping("/{auction-items_id}/likes")
    public ResponseEntity<ApiResponse<Void>> createAuctionItemLike(
        @PathVariable("auction-items_id") Long auctionItemId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        auctionItemLikeService.createAuctionItemLike(userId, auctionItemId);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("정상적으로 좋아요가 등록되었습니다."));
    }

    @DeleteMapping("/{auction-items_id}/likes")
    public ResponseEntity<ApiResponse<Void>> deleteAuctionItemLike(
        @PathVariable("auction-items_id") Long auctionItemId,
        @Auth AuthUser authUser
    ) {
        Long userId = authUser.id();
        auctionItemLikeService.hardDeleteAuctionItemLike(userId, auctionItemId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success("정상적으로 좋아요가 취소되었습니다."));
    }

}
