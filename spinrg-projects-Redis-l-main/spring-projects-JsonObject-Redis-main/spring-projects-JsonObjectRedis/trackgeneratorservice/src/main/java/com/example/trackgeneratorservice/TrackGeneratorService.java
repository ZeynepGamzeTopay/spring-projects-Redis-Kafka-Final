package com.example.trackgeneratorservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class TrackGeneratorService {

    private static final String CREATE_TRACK_TOPIC = "create_track";
    private static final String UPDATE_TRACK_TOPIC = "update_track";
    private final AtomicInteger trackId = new AtomicInteger(1);

    // Mevcut Track nesnelerini tutan Map
    private Map<Integer, Track> trackStore = new ConcurrentHashMap<>();

    @Autowired
    private KafkaTemplate<String, Track> kafkaTemplate;

    // Random nesnesi
    private Random random = new Random();

    // Platform türleri
    private static final String[] PLATFORM_TYPES = {"AWACS", "F16", "F35", "F22", "F15","F14"};
    private static final String[] CALLSIGN = {"KARTAL1", "BARIS3", "BILGE5","KAPLAN7", "HAKAN9"};

    // Rastgele yeni Track oluşturma
    public void createNewTrack() {
        Track newTrack = new Track(
                trackId.getAndIncrement(), // id (otomatik artan)
                randomHeading(), // rastgele heading
                randomPlatformType(), // rastgele platformType
                randomSpeed(), // rastgele speed
                randomCallsign(), // rastgele callsign
                randomLatitude(), // rastgele latitude
                randomLongitude(), // rastgele longitude
                randomAltitude() // rastgele altitude
        );
        trackStore.put(newTrack.getId(), newTrack); // Track'i Map'e ekle
        kafkaTemplate.send(CREATE_TRACK_TOPIC, newTrack);
        System.out.println("New track was created: " + newTrack);
    }

    // Rastgele heading (0.0 - 360.0 arası)
    private float randomHeading() {
        return random.nextFloat() * 360;
    }

    // Rastgele platformType seçimi
    private String randomPlatformType() {
        int index = random.nextInt(PLATFORM_TYPES.length);
        return PLATFORM_TYPES[index];
    }

    // Rastgele speed (0.0 - 1000.0 arası)
    private float randomSpeed() {
        return random.nextFloat() * 1000;
    }

    // Rastgele callsign seçimi

    private String randomCallsign() {
        int index = random.nextInt(CALLSIGN.length);
        return CALLSIGN[index];
    }

    // Rastgele latitude (-90.0 - 90.0 arası)
    private float randomLatitude() {
        return -90 + random.nextFloat() * 180;
    }

    // Rastgele longitude (-180.0 - 180.0 arası)
    private float randomLongitude() {
        return -180 + random.nextFloat() * 360;
    }

    // Rastgele altitude (0.0 - 20000.0 arası)
    private float randomAltitude() {
        return random.nextFloat() * 20000;
    }

    public void updateTrack(int id, float newHeading, String newPlatformType, float newSpeed, String newCallsign, float newLatitude, float newLongitude, float newAltitude) {
        // Track'in var olup olmadığını kontrol etme
        Track existingTrack = trackStore.get(id);
        System.out.println("Track: "+ existingTrack );
        if (existingTrack != null) {
            // Track özelliklerini güncelle
            existingTrack.setHeading(newHeading);
            existingTrack.setPlatformType(newPlatformType);
            existingTrack.setSpeed(newSpeed);
            existingTrack.setCallsign(newCallsign);
            existingTrack.setSpeed(newLatitude);
            existingTrack.setSpeed(newLongitude);
            existingTrack.setSpeed(newAltitude);

            kafkaTemplate.send(UPDATE_TRACK_TOPIC, existingTrack); // Güncellenmiş track'i gönder
            System.out.println("Track updated: " + existingTrack);
        } else {
            System.out.println("Track with ID " + id + " not found.");
        }
    }
    @KafkaListener(topics = "delete_track_from_service2_to_service1", groupId = "track_group")
    public void handleDeleteTrack(String trackId) {
        Integer id =Integer.parseInt(trackId);
        Track track= trackStore.get(id);
        System.out.println("Track: "+ track );
        if (track!= null) {
        trackStore.remove(id);  // HashMap'ten sil
        kafkaTemplate.send("delete_track_from_service1_to_service2", track);  // Servis 2'ye bildir
        System.out.println("Track "+ trackId+ " deleted from service 1 hash map" );}
    }
}

