package nbc.mushroom.domain.payment.dto;

public record PaymentReq(
    String paymentKey,
    String orderId,
    Long amount
) {

}
