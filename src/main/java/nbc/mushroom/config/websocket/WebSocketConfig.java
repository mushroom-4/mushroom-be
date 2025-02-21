package nbc.mushroom.config.websocket;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker // 메시지 브로커, 웹소켓 메시지 처리 활성화
@Configuration
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    // STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 엔트포인트. STOMP 접속 주소 url -> /ws (이 주소로 소켓 연결)
            .setAllowedOriginPatterns("*")  // CORS 설정 부분. (* : 모든 도메인 허용)
            .withSockJS(); // http 사용을 위해.
    }

    /**
     * 메시지 브로커.
     *
     * 클라이언트가 메시지 주고 받을 경로 지정
     * /pub: 클라이언트가 메시지를 보낼 때 사용하는 접두사 ( @MessageMapping과 연결. /pub + /@MessageMapping 엔드포인트 )
     * /sub: 클라이언트가 구독할 수 있는 주제(topic)의 접두사, 메시지 브로커를 통해 해당 주소를 구독하는 클라이언트로 전달 (브로드캐스트)
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(
            "/ws/pub"); // 클라이언트 -> 서버
        registry.enableSimpleBroker(
            "/ws/sub"); //  서버 -> 클라이언트
    }

    /**
     * STOMP 핸들러
     * CONNECT, SUBSCRIBE 등의 STOMP 프로토콜 명령어가 발생하면
     * 핸들러가 중간에 가로채서 어떠한 동작들을 수행하게 해줌
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }


    // @Auth 어노테이션 쓰기 위해 웹소켓 용 등록
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new StompAuthUserArgumentResolver());
    }
}
