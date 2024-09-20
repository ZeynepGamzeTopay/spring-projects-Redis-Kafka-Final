package com.example.rediswriterservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

@Service
public class TrackService {

    private final RedisTemplate<String, Track> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public TrackService(RedisTemplate<String, Track> redisTemplate, KafkaTemplate<String, String> kafkaTemplate) {
        this.redisTemplate = redisTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "delete_track_from_service3_to_service2", groupId = "track_group")
    public void handleDeleteTrackRequest(Track track) {
        // Send only track ID to Service 1
        kafkaTemplate.send("delete_track_from_service2_to_service1", String.valueOf(track.getId()));
        System.out.println("Track deletion info came from service 3 to service 2" );
    }

    @KafkaListener(topics = "delete_track_from_service1_to_service2", groupId = "track_group")
    public void handleDeleteTrackConfirmation(Track track) {
        // Delete the track from Redis using track ID
        redisTemplate.delete(String.valueOf(track.getId()));
        // Send deletion confirmation to Service 3 with track ID
        kafkaTemplate.send("delete_track_completed_from_service2_to_service3", String.valueOf(track.getId()));
        System.out.println("Track deletion info came from service 1 to service 2" );
    }
}
