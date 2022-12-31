package com.pavelkostal.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "position")
@Getter
@Setter
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "gps_position_latitude")
    private double gpsPositionLatitude;

    @Column(name = "gps_position_longitude")
    private double gpsPositionLongitude;

    @Column(name = "city", nullable = true, columnDefinition = "text")
    private String city;

    @Column(name = "region", nullable = false, columnDefinition = "text")
    private String region;

    @Column(name = "locality", nullable = false, columnDefinition = "text")
    private String locality;

    @Column(name = "country", nullable = false, columnDefinition = "text")
    private String country;

    @Column(name = "continent", nullable = false, columnDefinition = "text")
    private String continent;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "positon_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "position_id_fk"
            )

    )
    private Photo photo;

    public Position() {
    }

    public Position(double gpsPositionLatitude, double gpsPositionLongitude, String city, String region, String locality, String country, String continent) {
        this.gpsPositionLatitude = gpsPositionLatitude;
        this.gpsPositionLongitude = gpsPositionLongitude;
        this.city = city;
        this.region = region;
        this.locality = locality;
        this.country = country;
        this.continent = continent;
    }
}
