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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "unique_user_id", nullable = false, columnDefinition = "text")
    private String uniqueUserId;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(
            name = "position_id",
            referencedColumnName = "id"
    )
    private Position position;

    public Photo() {
    }

    public Photo(String photoAsString, String uniqueUserId, Position position) {
        this.uniqueUserId = uniqueUserId;
        this.position = position;
    }

}
