package com.example.rediswriterservice;

import com.example.rediswriterservice.Track;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Track> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Track> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Track objeleri için JSON serileştirici ayarla
        Jackson2JsonRedisSerializer<Track> serializer = new Jackson2JsonRedisSerializer<>(Track.class);
        template.setValueSerializer(serializer);
        return template;
    }
}

