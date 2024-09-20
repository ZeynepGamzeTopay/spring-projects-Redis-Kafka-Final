package com.example.trackgeneratorservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Track {
    private int id;
    private float heading;
    private String platformType;
    private float speed;
    private String callsign;
    private float latitude;
    private float longitude;
    private float altitude;
}
