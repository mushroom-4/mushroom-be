package nbc.mushroom.domain.payment.dto;

import java.util.Map;

public record PaymentRes(
    String orderName,
    String orderId,
    String paymentKey,
    Long amount
) {

    public static PaymentRes from(Map<String, Object> paymentData) {
        return new PaymentRes(
            (String) paymentData.get("orderName"),
            (String) paymentData.get("orderId"),
            (String) paymentData.get("paymentKey"),
            Long.valueOf((Integer) paymentData.get("totalAmount"))
        );
    }
}
