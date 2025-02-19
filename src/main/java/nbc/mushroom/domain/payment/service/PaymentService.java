package nbc.mushroom.domain.payment.service;

import static nbc.mushroom.domain.common.exception.ExceptionType.BID_NOT_FOUND;
import static nbc.mushroom.domain.common.exception.ExceptionType.SERVER_PAYMENT_FAIL;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import nbc.mushroom.domain.bid.entity.Bid;
import nbc.mushroom.domain.bid.repository.BidRepository;
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

    // HTTP 요청을 보내기 위한 RestTemplate 객체
    private final RestTemplate restTemplate;

    // 테스트용 토스페이 비밀키 (실제 운영에서는 환경 변수로 관리해야 함)
    private static final String WIDGET_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

    // 결제 승인 요청을 보낼 토스페이 API 엔드포인트 URL
    private static final String WIDGET_URL = "https://api.tosspayments.com/v1/payments/confirm";

    // 결제 취소 요청을 보낼 토스페이 API 엔드포인트 URL
    private static final String CANCEL_URL = "https://api.tosspayments.com/v1/payments/{paymentKey}/cancel";

    // 토스페이 API에 인증을 위한 Base64 인코딩된 Secret Key (Basic Auth 방식)
    private static final String WIDGET_SECRET_KEY_ENCODED = Base64.getEncoder()
        .encodeToString((WIDGET_SECRET_KEY + ":").getBytes(StandardCharsets.UTF_8));

    private final BidRepository bidRepository;

    @Transactional
    public PaymentRes confirmPayment(PaymentReq paymentReq) {
        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + WIDGET_SECRET_KEY_ENCODED);

        // HTTP 요청 엔티티 생성 (결제 요청 데이터 + 헤더 포함)
        HttpEntity<PaymentReq> request = new HttpEntity<>(paymentReq, headers);

        try {
            // 토스페이 API에 결제 승인 요청 전송 (POST 방식)
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                WIDGET_URL,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                } // 응답 타입을 Map<String, Object>로 설정
            );
            // 응답 데이터가 null이 아닐 경우, PaymentRes 객체로 변환
            PaymentRes paymentRes = PaymentRes.from(Objects.requireNonNull(response.getBody()));

            // 서비스 로직
            Long bidId = Long.valueOf(paymentRes.orderId().substring(20));
            Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new CustomException(BID_NOT_FOUND));

            bid.paymentComplete(paymentRes.amount());

            return paymentRes;
        } catch (Exception e) {
            // 결제 승인 요청이 실패할 경우, 예외를 발생시킴
            if (e instanceof RestClientException) {
                throw new CustomException(SERVER_PAYMENT_FAIL);
            }

            cancelPayment(paymentReq.paymentKey(), e.getMessage(), paymentReq.amount());
            throw e;
        }
    }

    private void cancelPayment(String paymentKey, String reason, Long cancelAmount) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + WIDGET_SECRET_KEY_ENCODED);

        // 결제 취소 요청 본문 생성
        Map<String, Object> body = Map.of(
            "cancelReason", reason,
            "cancelAmount", cancelAmount
        );
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        try {
            restTemplate.exchange(
                CANCEL_URL.replace("{paymentKey}", paymentKey), // URL에 결제 키 삽입
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
            );
        } catch (RestClientException e) {
            throw new RuntimeException("결제 취소 중 오류가 발생했습니다.");
        }
    }
}
