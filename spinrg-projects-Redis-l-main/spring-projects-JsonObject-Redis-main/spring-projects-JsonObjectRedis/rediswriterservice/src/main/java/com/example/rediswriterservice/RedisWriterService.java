package com.example.rediswriterservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class RedisWriterService {

    private static final String REDIS_TRACK_TOPIC = "new_redis_track_was_created";
    private static final String REDIS_UPDATE_TOPIC = "updates_from_redis_track";

    @Autowired
    private RedisTemplate<String, Track> redisTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "create_track", groupId = "track_group")
    public void listenCreateTrack(Track track) {
        System.out.println("Received new track from 1st service: " + track);
        redisTemplate.opsForValue().set(String.valueOf(track.getId()), track);  // JSON olarak yaz
        kafkaTemplate.send(REDIS_TRACK_TOPIC, String.valueOf(track.getId()));
        System.out.println("Track ID was sent to 3rd service: " + track.getId());
    }

    @KafkaListener(topics = "update_track", groupId = "track_group")
    public void listenUpdateTrack(Track track) {
        System.out.println("Received updated track from 1st service: " + track);
        redisTemplate.opsForValue().set(String.valueOf(track.getId()), track);  // JSON olarak güncelle
        kafkaTemplate.send(REDIS_UPDATE_TOPIC, String.valueOf(track.getId()));
        System.out.println("Updated track ID was sent to 3rd service: " + track.getId());
    }
}
