package nbc.mushroom.domain.notice.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.notice.dto.response.ReadNoticeRes;
import nbc.mushroom.domain.notice.service.NoticeReadService;
import nbc.mushroom.domain.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/notices")
public class NoticeController {

    private final NoticeReadService noticeReadService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReadNoticeRes>>> getAllNotice(
        @Auth AuthUser authUser
    ) {
        User user = User.fromAuthUser(authUser);
        List<ReadNoticeRes> readNoticeResList = noticeReadService
            .getAllNoticeByUser(user);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("사용자의 공지를 조회했습니다.", readNoticeResList));
    }
}
