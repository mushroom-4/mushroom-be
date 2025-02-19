package nbc.mushroom.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.notice.dto.SearchPageNoticeRes;
import nbc.mushroom.domain.user.entity.User;
import nbc.mushroom.domain.user.service.UserNoticeService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/notices")
public class UserNoticeControllerV2 {

    private final UserNoticeService userNoticeService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SearchPageNoticeRes>>> test(
        @Auth AuthUser authUser,
        @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        User user = User.fromAuthUser(authUser);
        Page<SearchPageNoticeRes> searchNoticeResList = userNoticeService.searchUserNotice(user,
            page);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
            "사용자의 공지를 조회했습니다.", searchNoticeResList));
    }
}
