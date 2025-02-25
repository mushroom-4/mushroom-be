package nbc.mushroom.domain.notice.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.notice.dto.ReadNoticeRes;
import nbc.mushroom.domain.notice.service.NoticeReadService;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users/notices")
public class NoticeControllerV2 {

    private final NoticeReadService noticeReadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadNoticeRes>>> getUserNotice(
        @Auth AuthUser authUser
    ) {
        User user = User.fromAuthUser(authUser);
        List<ReadNoticeRes> readNoticeResList = noticeReadService
            .searchUserNotice(user);

        return ResponseEntity
            .ok(ApiResponse.success("사용자의 공지를 조회했습니다.", readNoticeResList));
    }
}
