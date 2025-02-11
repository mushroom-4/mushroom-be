package nbc.mushroom.domain.user.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.user.dto.request.UserPasswordChangeRequest;
import nbc.mushroom.domain.user.dto.response.UserResponse;
import nbc.mushroom.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable long userId) {
        UserResponse userResponse = userService.getUser(userId);

        return ResponseEntity
            .ok(ApiResponse.success("유저가 정상적으로 조회되었습니다.", userResponse));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> changePassword(
        @Auth AuthUser authUser,
        @RequestBody UserPasswordChangeRequest userPasswordChangeRequest
    ) {
        userService.changePassword(authUser.id(), userPasswordChangeRequest);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(ApiResponse.success("유저의 비밀번호가 정상 변경되었습니다."));
    }
}
