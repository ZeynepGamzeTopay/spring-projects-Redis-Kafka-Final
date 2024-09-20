package com.example.redisreaderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Track> kafkaTemplate;

    public void sendDeleteTrackMessage(Track track) {
        kafkaTemplate.send("delete_track_from_service3_to_service2", track);
    }
}
