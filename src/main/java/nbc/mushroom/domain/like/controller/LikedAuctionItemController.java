package nbc.mushroom.domain.like.controller;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.like.dto.response.CheckLikedAuctionItemRes;
import nbc.mushroom.domain.like.dto.response.LikedAuctionItemRes;
import nbc.mushroom.domain.like.service.LikedAuctionItemService;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/auction-items")
public class LikedAuctionItemController {

    private final LikedAuctionItemService likedAuctionItemService;

    // 본인이 누른 경매 물품 좋아요 목록 API
    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<Page<LikedAuctionItemRes>>> getAllLikedAuctionItem(
        @Auth AuthUser authUser,
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        User user = User.fromAuthUser(authUser);
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<LikedAuctionItemRes> auctionItems = likedAuctionItemService.getAllLikedAuctionItem(
            user,
            pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("사용자가 좋아요한 경매 물품에 조회 성공했습니다.", auctionItems));
    }
    
    // 본인이 해당 경매 물품에, 좋아요를 한 여부를 확인해주는 API
    @GetMapping("/{auction-items_id}/likes")
    public ResponseEntity<ApiResponse<CheckLikedAuctionItemRes>> checkLikedAuctionItem(
        @Auth AuthUser authUser,
        @PathVariable("auction-items_id") Long auctionItemId
    ) {
        User user = User.fromAuthUser(authUser);
        CheckLikedAuctionItemRes auctionItems = likedAuctionItemService.getLikedAuctionItem(
            user, auctionItemId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("해당 경매 물품에 유저의 좋아요 여부를 확인했습니다.",
                auctionItems));
    }

}
