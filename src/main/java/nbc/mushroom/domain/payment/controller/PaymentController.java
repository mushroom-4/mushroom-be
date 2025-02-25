package nbc.mushroom.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.annotation.Auth;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.payment.dto.request.PaymentReq;
import nbc.mushroom.domain.payment.dto.response.PaymentRes;
import nbc.mushroom.domain.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm/widget")
    public ResponseEntity<ApiResponse<PaymentRes>> confirmPayment(
        @Auth AuthUser authUser,
        @RequestBody PaymentReq paymentReq
    ) {
        PaymentRes paymentRes = paymentService.confirmPayment(authUser, paymentReq);

        return ResponseEntity.status(HttpStatus.OK)
            .body(ApiResponse.success("결제가 정상적으로 완료되었습니다.", paymentRes));
    }
}
