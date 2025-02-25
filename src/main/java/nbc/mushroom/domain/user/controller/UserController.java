package nbc.mushroom.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auth.dto.response.TokenRes;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.dto.request.UpdateUserInfoReq;
import nbc.mushroom.domain.user.dto.request.UpdateUserPasswordReq;
import nbc.mushroom.domain.user.dto.response.UserRes;
import nbc.mushroom.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/info")
    public ResponseEntity<ApiResponse<UserRes>> getUser(@PathVariable long userId) {
        UserRes userRes = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("유저가 정상적으로 조회되었습니다.", userRes));
    }

    @PutMapping(value = "/info", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TokenRes>> updateUserInfo(
        @Auth AuthUser authUser,
        @Valid @ModelAttribute UpdateUserInfoReq updateUserInfoReq
    ) {
        TokenRes tokenRes = userService.updateUserInfo(authUser, updateUserInfoReq);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("유저의 정보가 정상적으로 변경되었습니다.", tokenRes));
    }

    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
        @Auth AuthUser authUser,
        @Valid @RequestBody UpdateUserPasswordReq updateUserPasswordReq
    ) {
        userService.updatePassword(authUser.id(), updateUserPasswordReq);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success("유저의 비밀번호가 정상 변경되었습니다."));
    }
}
