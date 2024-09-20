package com.example.redisreaderservice;

import com.example.redisreaderservice.Track;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Track> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Track> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Track objeleri için JSON serileştirici ayarlari
        Jackson2JsonRedisSerializer<Track> serializer = new Jackson2JsonRedisSerializer<>(Track.class);
        template.setValueSerializer(serializer);
        return template;
    }
}

