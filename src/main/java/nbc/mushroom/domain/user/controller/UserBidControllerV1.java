package nbc.mushroom.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.dto.response.UserBidRes;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.service.UserBidService;
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
@RequestMapping("/api/v1/users/bids")
@RequiredArgsConstructor
public class UserBidControllerV1 {

    private final UserBidService userBidService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserBidRes>>> getUserBidHistory(
        @Auth AuthUser authUser,
        @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 10);
        User loginUser = User.fromAuthUser(authUser);

        Page<UserBidRes> getUserBidRes = userBidService.getUserBidHistory(loginUser, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("입찰 내역 목록 조회에 성공했습니다", getUserBidRes));
    }
}
