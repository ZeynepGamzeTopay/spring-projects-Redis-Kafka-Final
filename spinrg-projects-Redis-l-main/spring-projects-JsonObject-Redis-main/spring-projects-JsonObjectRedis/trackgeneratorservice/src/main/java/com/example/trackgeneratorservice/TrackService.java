package com.example.trackgeneratorservice;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TrackService {

    private final KafkaTemplate<String, Track> kafkaTemplate;
    private final ConcurrentHashMap<String, Track> trackMap;

    public TrackService(KafkaTemplate<String, Track> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.trackMap = new ConcurrentHashMap<>();
    }

}