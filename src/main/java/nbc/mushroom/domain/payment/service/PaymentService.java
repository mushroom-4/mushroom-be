package nbc.mushroom.domain.payment.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.SERVER_PAYMENT_FAIL;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.common.exception.CustomException;
import nbc.mushroom.domain.payment.dto.PaymentReq;
import nbc.mushroom.domain.payment.dto.PaymentRes;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final RestTemplate restTemplate;
    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"; // 테스트키라서 은닉화 안함
    private static final String WIDGET_URL = "https://api.tosspayments.com/v1/payments/confirm";
    private static final String WIDGET_SECRET_KEY_ENCODED = Base64.getEncoder()
        .encodeToString((WIDGET_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

    @Transactional
    public PaymentRes confirmPayment(PaymentReq paymentReq) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + WIDGET_SECRET_KEY_ENCODED);

        HttpEntity<PaymentReq> request = new HttpEntity<>(paymentReq, headers);

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                WIDGET_URL,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
            );
            PaymentRes paymentRes = PaymentRes.from(Objects.requireNonNull(response.getBody()));
            return paymentRes;
        } catch (RestClientException e) {
            throw new CustomException(SERVER_PAYMENT_FAIL);
        }
    }
}
