package com.pavelkostal.api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "Photo")
@Table(name = "photo")
@Data
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "photo_owner")
    private String photoOwner;

    @Column(name = "gps_position_latitude")
    private Double gpsPositionLatitude;

    @Column(name = "gps_position_longitude")
    private Double gpsPositionLongitude;

    @Column(name = "city", columnDefinition = "text")
    private String city;

    @Column(name = "region", columnDefinition = "text")
    private String region;

    @Column(name = "locality", columnDefinition = "text")
    private String locality;

    @Column(name = "country", columnDefinition = "text")
    private String country;

    @Column(name = "continent", nullable = false, columnDefinition = "text")
    private String continent;

    public Photo() {
    }

    public Photo(String photoOwner, double gpsPositionLatitude, double gpsPositionLongitude, String city, String region, String locality, String country, String continent) {
        this.photoOwner = photoOwner;
        this.gpsPositionLatitude = gpsPositionLatitude;
        this.gpsPositionLongitude = gpsPositionLongitude;
        this.city = city;
        this.region = region;
        this.locality = locality;
        this.country = country;
        this.continent = continent;
    }
}
