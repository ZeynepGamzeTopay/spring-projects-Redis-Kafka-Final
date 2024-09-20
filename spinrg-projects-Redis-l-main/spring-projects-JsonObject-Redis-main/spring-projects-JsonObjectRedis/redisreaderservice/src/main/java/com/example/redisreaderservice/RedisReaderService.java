package com.example.redisreaderservice;

        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.kafka.annotation.KafkaListener;
        import org.springframework.kafka.core.KafkaTemplate;
        import org.springframework.stereotype.Service;
        import org.springframework.data.redis.core.RedisTemplate;

        import java.util.Scanner;

@Service
public class RedisReaderService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final RedisTemplate<String, Track> redisTemplate;

    //private final TerminalInputService terminalInputService;
    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    public RedisReaderService(RedisTemplate<String, Track> redisTemplate) {
        this.redisTemplate = redisTemplate;

    }

    @KafkaListener(topics = "new_redis_track_was_created", groupId = "track_group")
    public void listenNewRedisTrack(String trackId) {
        Track track = redisTemplate.opsForValue().get(trackId);  // JSON olarak oku
        System.out.println("Retrieved track from Redis: " + track);

        TrackDeletion();
    }

    @KafkaListener(topics = "updates_from_redis_track", groupId = "track_group")
    public void listenUpdatedRedisTrack(String trackId) {
        Track track = redisTemplate.opsForValue().get(trackId);  // JSON olarak oku
        System.out.println("Retrieved updated track from Redis: " + track);

        TrackDeletion();
    }
    @KafkaListener(topics = "delete_track_completed_from_service2_to_service3", groupId = "track_group")
    public void listenTrackDeleted(String trackId) {
        System.out.println("Track with ID " + trackId + " was deleted.");


    }
    public void TrackDeletion() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to delete a track? (yes/no): ");
        String confirmation = scanner.nextLine();
        if ("yes".equalsIgnoreCase(confirmation)) {
          System.out.println("Enter the Track ID to delete: ");
          String trackId = scanner.nextLine();
          Track track = redisTemplate.opsForValue().get(trackId);  // JSON olarak oku
            if (track != null) {
                kafkaProducerService.sendDeleteTrackMessage(track);
                System.out.println("Track with ID " + trackId + " will be deleted.");
            } else {
                System.out.println("Track not found for ID: " + trackId);
            }
         //redisReaderService.TrackDeletion(trackId);
          System.out.println("Sent delete request for track ID: " + trackId);
        } else {
           System.out.println("Delete operation cancelled.");
        }
    }
}
