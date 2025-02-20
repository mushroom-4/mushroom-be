package nbc.mushroom.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
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
}
