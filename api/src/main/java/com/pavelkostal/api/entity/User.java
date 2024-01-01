package com.pavelkostal.api.entity;


import com.pavelkostal.api.model.ResponsePhoto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "User")
@Table(name = "user_information")
@Getter
@Setter
public class User implements ResponsePhoto {
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
            name = "photo_id",
            referencedColumnName = "id"
    )
    private Photo photo;

    public User() {
    }

    public User(String uniqueUserId, Photo photo) {
        this.uniqueUserId = uniqueUserId;
        this.photo = photo;
    }

}
