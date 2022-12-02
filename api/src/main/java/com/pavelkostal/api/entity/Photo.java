package com.pavelkostal.api.entity;


import jakarta.persistence.*;

@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "photo", nullable = false)
    private String photoAsString;

    @Column(name = "gps_position_latitude")
    private double gpsPositionLatitude;

    @Column(name = "gps_position_longitude")
    private double gpsPositionLongitude;

    public Photo() {
    }

    public Photo(String photoAsString, double gpsPositionLatitude, double gpsPositionLongitude) {
        this.photoAsString = photoAsString;
        this.gpsPositionLatitude = gpsPositionLatitude;
        this.gpsPositionLongitude = gpsPositionLongitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoAsString() {
        return photoAsString;
    }

    public void setPhotoAsString(String photoAsString) {
        this.photoAsString = photoAsString;
    }

    public double getGpsPositionLatitude() {
        return gpsPositionLatitude;
    }

    public void setGpsPositionLatitude(double gpsPosition) {
        this.gpsPositionLatitude = gpsPosition;
    }

    public double getGpsPositionLongitude() {
        return gpsPositionLongitude;
    }

    public void setGpsPositionLongitude(double gpsPositionLongitude) {
        this.gpsPositionLongitude = gpsPositionLongitude;
    }
}
