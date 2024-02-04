package com.pavelkostal.api.repository;

import com.pavelkostal.api.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Query("SELECT DISTINCT(country) FROM Photo WHERE country IS NOT NULL")
    List<String> findAllCountries();

    @Query("SELECT DISTINCT(city) FROM Photo WHERE country = ?1 AND city IS NOT NULL")
    List<String> findAllCiyByCountry(String country);

    @Query("SELECT photo FROM Photo photo WHERE photo.country = ?1 AND photo.city = ?2")
    List<Photo> findAllPhotosByCountryAndCity(String country, String city);

    @Query("SELECT photo FROM Photo photo WHERE photo.photoOwner = ?1")
    List<Photo> findPhotosByUniqueUserId(String uniqueUserId);
}
