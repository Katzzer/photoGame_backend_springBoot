package com.pavelkostal.api.repository;

import com.pavelkostal.api.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT city FROM Photo ")
    List<String> findAllCity();

    @Query("SELECT photo FROM Photo photo WHERE photo.city = ?1")
    List<Photo> findAllPhotosByCity(String city);

    @Query("SELECT photo FROM Photo photo WHERE photo.photoOwner = ?1")
    List<Photo> findPhotosByUniqueUserId(String uniqueUserId);
}
