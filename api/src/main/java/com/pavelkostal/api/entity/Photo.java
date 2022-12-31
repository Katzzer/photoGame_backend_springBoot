package com.pavelkostal.api.entity;


import com.pavelkostal.api.model.ResponsePhoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "Photo")
@Table(name = "photo")
@Getter
@Setter
public class Photo implements ResponsePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "photo", nullable = false, columnDefinition = "text")
    private String photoAsString;

    @Column(name = "uniquer_user_id", nullable = false, columnDefinition = "text")
    private String uniqueUserId;

    @OneToOne(
            mappedBy = "photo",
            orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.EAGER
    )
    private Position position;

    public Photo() {
    }

    public Photo(String photoAsString, String uniqueUserId, Position position) {
        this.photoAsString = photoAsString;
        this.uniqueUserId = uniqueUserId;
        this.position = position;
    }

}
