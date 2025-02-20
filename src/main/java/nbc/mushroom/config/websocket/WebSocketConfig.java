package nbc.mushroom.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker // 메시지 브로커, 웹소켓 메시지 처리 활성화
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    // STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // http 용
        registry.addEndpoint("/ws") // 엔트포인트. STOMP 접속 주소 url -> /ws (이 주소로 소켓 연결)
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOriginPatterns("*")  // CORS 설정 부분. (* : 모든 도메인 허용) -> 추후 지정하든가 하자..
            .withSockJS(); // http 사용을 위해.

        // /ws 용
        registry.addEndpoint("/ws")
            .addInterceptors(jwtHandshakeInterceptor)
            .setAllowedOriginPatterns("*");
    }

    /**
     * 메시지 브로커.
     *
     * 클라이언트가 메시지 주고 받을 경로 지정
     * /pub: 클라이언트가 메시지를 보낼 때 사용하는 접두사 ( @MessageMapping과 연결. /pub + /@MessageMapping 엔드포인트 )
     * /sub: 클라이언트가 구독할 수 있는 주제(topic)의 접두사, 메시지 브로커를 통해 해당 주소를 구독하는 클라이언트로 전달 (라우팅)
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(
            "/pub"); // 클라이언트 -> 서버
        registry.enableSimpleBroker(
            "/sub"); //  서버 -> 클라이언트
    }
}
