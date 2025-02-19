package nbc.mushroom.domain.payment.dto;

import java.util.Map;

public record PaymentRes(
    String orderName,
    String orderId,
    String paymentKey,
    Integer amount
) {

    public static PaymentRes from(Map<String, Object> paymentData) {
        return new PaymentRes(
            (String) paymentData.get("orderName"),
            (String) paymentData.get("orderId"),
            (String) paymentData.get("paymentKey"),
            (Integer) paymentData.get("totalAmount")
        );
    }
}
