package com.pavelkostal.api.service;

import com.pavelkostal.api.entity.Photo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PhotoServiceTest {

    @Autowired
    PhotoService underTest;

    @Test
    void savePhoto() {
        // Given
        // When
        // Then
    }

    @Test
    void getPhotoById() {
        // Given
        // When
        // Then
    }

    @Test
    void getAllImagesForSelectedUser() {
        // Given
        // When
        // Then
    }

    @Test
    void findAllCountries() {
        // Given
        // When
        ResponseEntity<List<String>> allCountriesResponse = underTest.findAllCountries();
        // Then
        List<String> allCountries = allCountriesResponse.getBody();
        assertNotNull(allCountries);
        assertEquals(1, allCountries.size());
    }

    @Test
    void findAllCityByCountry() {
        // Given
        String country = "Czech republic";
        String cityHk = "Hradec Kralove";
        String cityPrague = "Prague";
        // When
        ResponseEntity<List<String>> allCityByCountry = underTest.findAllCityByCountry(country);
        // Then
        List<String> allCityByCountryResponseBody = allCityByCountry.getBody();
        assertNotNull(allCityByCountryResponseBody);
        assertTrue(allCityByCountryResponseBody.contains(cityHk));
        assertTrue(allCityByCountryResponseBody.contains(cityPrague));
    }

    @Test
    void findAllPhotosByCountryAndCity() {
        // Given
        String country = "Czech republic";
        String city = "Prague";
        // When
        ResponseEntity<List<Photo>> allPhotosByCountryAndCity = underTest.findAllPhotosByCountryAndCity(country, city);
        // Then
        List<Photo> resultListOfPhotos = allPhotosByCountryAndCity.getBody();
        assertNotNull(resultListOfPhotos);
        assertEquals(2, resultListOfPhotos.size());
    }
}