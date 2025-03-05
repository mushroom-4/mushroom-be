package nbc.mushroom.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nbc.mushroom.domain.chat.service.RedisSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisChatConfig {

    private static final String CHANNEL_NAME = "chatroom";

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() { // 필수 설정은 아님
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * Redis 템플릿 - Object
     *
     * @param redisConnectionFactory : Redis 연결
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // key 직렬화 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 자바의 날짜/시간 API 올바르게 사용할 수 있도록
        objectMapper.disable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // YYYY-MM-DDTHH:mm:ss 으로 표현
        objectMapper.enable(
            SerializationFeature.INDENT_OUTPUT); // 읽기 쉽게 들여쓰기 활성화 (디버깅 용도, 베포 시엔 성능을 위해 비활성화)

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(
            objectMapper, Object.class);

        // value 직렬화 설정
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        return redisTemplate;
    }

    /**
     * pub/sub 메시지 처리를 위한 설정.
     * <p>
     * 특정 채널의 메시지가 수신되면 이 메서드를 통해서 RedisSubscriber의 sendMessage 메서드가 호출됨
     *
     * @param redisConnectionFactory : Redis 연결
     * @param messageListenerAdapter : Redis 메시지 수신 어댑터 (수신 시 호출할 메서드 지정)
     * @param channelTopic           : 구독할 Redis 채널 설정
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory redisConnectionFactory,
        MessageListenerAdapter messageListenerAdapter,
        ChannelTopic channelTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter,
            channelTopic);
        return container;
    }

    /**
     * Redis 메시지 리스너 어댑터 설정
     * <p>
     * 어댑터 사용 시 RedisSubscriber의 onMessage 메서드가 호출됨
     *
     * @param redisSubscriber : 메시지를 처리하는 비즈니스 로직 클래스 (domain.chat.service 에 위치)
     */
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "onMessage");
    }

    /**
     * Redis 채널(토픽) 설정
     * <p>
     * Redis에서 메시지를 발행/구독 확인 시 사용할 채널 이름 지정
     * <p>
     * Redis 내부의 Pub/Sub 시스템에서 사용되는 식별자
     */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(CHANNEL_NAME);
    }
}
