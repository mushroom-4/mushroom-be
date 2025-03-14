package nbc.mushroom.domain.payment.service;

import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.service.BidService;
import nbc.mushroom.domain.common.dto.AuthUser;
import nbc.mushroom.domain.payment.dto.request.PaymentReq;
import nbc.mushroom.domain.payment.dto.response.PaymentRes;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final PaymentService paymentService;
    private final BidService bidService;

    public PaymentRes confirmPayment(AuthUser authUser, PaymentReq paymentReq) {
        bidService.paymentConfirm(authUser, paymentReq);
        try {
            return PaymentRes.from(paymentService.sendPayment(paymentReq));
        } catch (Exception e) {
            bidService.paymentCancel(paymentReq);
            throw e;
        }
    }
}
