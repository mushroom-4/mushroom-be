package nbc.mushroom.domain.payment.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentReq(
    @NotBlank
    String paymentKey,

    @NotBlank
    String orderId,

    @Min(1_000)
    @NotNull
    Long amount
) {

}
