package com.pavelkostal.api.entity;


import com.pavelkostal.api.model.ResponsePhoto;
import jakarta.persistence.*;

@Entity
public class Photo implements ResponsePhoto {
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
    
    @Column(name = "uniquer_user_id")
    private String uniqueUserId;

    public Photo() {
    }
    
    public Photo(String photoAsString, double gpsPositionLatitude, double gpsPositionLongitude, String uniqueUserId) {
        this.photoAsString = photoAsString;
        this.gpsPositionLatitude = gpsPositionLatitude;
        this.gpsPositionLongitude = gpsPositionLongitude;
        this.uniqueUserId = uniqueUserId;
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
    
    public String getUniqueUserId() {
        return uniqueUserId;
    }
    
    public void setUniqueUserId(String uniqueUserId) {
        this.uniqueUserId = uniqueUserId;
    }
}
