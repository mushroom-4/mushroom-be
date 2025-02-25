package nbc.mushroom.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.auth.dto.request.UserRegisterReq;
import nbc.mushroom.domain.auth.dto.response.TokenRes;
import nbc.mushroom.domain.auth.service.AuthService;
import nbc.mushroom.domain.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthControllerV2 {

    private final AuthService authService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TokenRes>> register(
        @Valid @ModelAttribute UserRegisterReq userRegisterReq
    ) {
        TokenRes tokenRes = authService.register(userRegisterReq);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success("정상적으로 회원가입 되었습니다.", tokenRes));
    }
}
