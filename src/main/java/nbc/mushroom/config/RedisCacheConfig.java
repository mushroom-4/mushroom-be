package nbc.mushroom.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

    /**
     * StringRedisSerializer - Key 직렬화 GenericJackson2JsonRedisSerializer - Value 직렬화
     */
//    @Bean
//    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//            .entryTtl(Duration.ofHours(1))
//            .serializeKeysWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                    new StringRedisSerializer())) // Key 직렬화 설정
//            .serializeValuesWith(
//                RedisSerializationContext.SerializationPair.fromSerializer(
//                    new GenericJackson2JsonRedisSerializer())); // Value 직렬화 설정
//
//        return RedisCacheManager.builder(redisConnectionFactory)
//            .cacheDefaults(config)
//            .build();
//    }
    @Bean
    public RedisTemplate<String, String> redisCacheTemplate(
        RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
