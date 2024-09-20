package com.example.trackgeneratorservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackController {

    @Autowired
    private TrackGeneratorService trackService;

    @GetMapping("/createTrack")
    public String createTrack() {
        trackService.createNewTrack();
        return "Track creation initiated!";
    }

    @GetMapping("/updateTrack")
    public String updateTrack() {
        trackService.updateTrack(3, 270.0f, "AWACS", 300.0f, "E", 53.0f, 14.0f, 8000.0f);
        return "Track update initiated!";
    }
}
