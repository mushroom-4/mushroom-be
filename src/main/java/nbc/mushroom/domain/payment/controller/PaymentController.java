package nbc.mushroom.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.dto.ApiResponse;
import nbc.mushroom.domain.payment.dto.PaymentReq;
import nbc.mushroom.domain.payment.dto.PaymentRes;
import nbc.mushroom.domain.payment.service.PaymentService;
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
        @RequestBody PaymentReq paymentReq
    ) {
        PaymentRes paymentRes = paymentService.confirmPayment(paymentReq);
        
        return ResponseEntity
            .ok(ApiResponse.success("결제가 정상적으로 완료되었습니다.", paymentRes));
    }
}
