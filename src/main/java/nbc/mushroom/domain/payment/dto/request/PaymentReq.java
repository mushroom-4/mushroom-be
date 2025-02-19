package nbc.mushroom.domain.payment.dto.request;

public record PaymentReq(
    String paymentKey,
    String orderId,
    Long amount
) {

}
