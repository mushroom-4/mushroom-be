package nbc.mushroom.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auth.dto.request.UserLoginReq;
import nbc.mushroom.domain.auth.dto.request.UserRegisterReq;
import nbc.mushroom.domain.auth.dto.response.TokenRes;
import nbc.mushroom.domain.auth.service.AuthService;
import nbc.mushroom.domain.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TokenRes>> register(
        @Valid @ModelAttribute UserRegisterReq userRegisterReq
    ) {
        TokenRes tokenRes = authService.register(userRegisterReq);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("정상적으로 회원가입 되었습니다.", tokenRes));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenRes>> login(
        @Valid @RequestBody UserLoginReq userLoginReq
    ) {
        TokenRes tokenRes = authService.login(userLoginReq);
        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("정상적으로 로그인 되었습니다.", tokenRes));
    }
}
