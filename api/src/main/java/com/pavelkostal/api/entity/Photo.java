package com.pavelkostal.api.entity;


import com.pavelkostal.api.model.ResponsePhoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Photo implements ResponsePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "photo", nullable = false, columnDefinition = "text")
    private String photoAsString;

    @Column(name = "gps_position_latitude")
    private double gpsPositionLatitude;

    @Column(name = "gps_position_longitude")
    private double gpsPositionLongitude;

    @Column(name = "city")
    private String city;
    
    @Column(name = "uniquer_user_id")
    private String uniqueUserId;

    public Photo() {
    }

    public Photo(String photoAsString, double gpsPositionLatitude, double gpsPositionLongitude, String city, String uniqueUserId) {
        this.photoAsString = photoAsString;
        this.gpsPositionLatitude = gpsPositionLatitude;
        this.gpsPositionLongitude = gpsPositionLongitude;
        this.city = city;
        this.uniqueUserId = uniqueUserId;
    }
}
