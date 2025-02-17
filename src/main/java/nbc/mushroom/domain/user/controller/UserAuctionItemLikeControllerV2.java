package nbc.mushroom.domain.user.controller;


import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.dto.response.SearchUserAuctionItemLikeRes;
import nbc.mushroom.domain.user.service.UserAuctionItemLikeService;
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
@RequestMapping("/api/v2/users/auction-items/likes")
public class UserAuctionItemLikeControllerV2 {

    private final UserAuctionItemLikeService userAuctionItemLikeService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchUserAuctionItemLikeRes>>> searchUserLike(
        @Auth AuthUser authUser,
        @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        Long userId = authUser.id();
        Pageable pageable = PageRequest.of(page - 1, 10);
        Page<SearchUserAuctionItemLikeRes> searchUserAuctionItemLikeRes = userAuctionItemLikeService.searchUserLike(
            userId,
            pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("조회에 성공했습니다.", searchUserAuctionItemLikeRes));
    }
}
