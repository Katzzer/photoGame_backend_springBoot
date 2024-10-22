package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.utils.tools.GPSPositionTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GPSPositionToolsTest {

    @Autowired
    GPSPositionTools underTest;
    
    double latitudeForPosition1 = 50.2092567;
    double longitudeForPosition1 = 15.8327564;
    Photo photo1 = new Photo("1", latitudeForPosition1, longitudeForPosition1,"Hradec Kralove",null, null, null, null);

    double latitudeForPosition2 = 50.0486575;
    double longitudeForPosition2 = 14.5111506;
    Photo photo2 = new Photo("2", latitudeForPosition2, longitudeForPosition2, "Prague",null, null, null, null);

    @Test
    void itShouldTestCorrectGps() {
        // Given
        double latitude =  50.2092567;
        double longitude = 15.8327564;

        // When
        boolean validGps = underTest.isValidGps(latitude, longitude);

        // Then
        assertTrue(validGps);
    }

    @Test
    void itShouldTestIncorrectGps() {
        // Given
        double latitude =  50.2092567;
        double longitude = 190;

        // When
        boolean validGps = underTest.isValidGps(latitude, longitude);

        // Then
        assertFalse(validGps);
    }

    @Test
    void getPositionFromGps() {
        // Given

        // When
        underTest.setPositionInformationFromGpsOrCityToCurrentPhoto(photo1);
        underTest.setPositionInformationFromGpsOrCityToCurrentPhoto(photo2);

        // Then
        assertEquals(longitudeForPosition1, photo1.getGpsPositionLongitude());
        assertEquals(latitudeForPosition1, photo1.getGpsPositionLatitude());
        assertEquals("Hradec Kralove", photo1.getCity());
        assertEquals("Hradec Kralove", photo1.getRegion());
        assertEquals("Hradec Kralove", photo1.getLocality());
        assertEquals("Czechia", photo1.getCountry());
        assertEquals("Europe", photo1.getContinent());

        assertEquals(longitudeForPosition2, photo2.getGpsPositionLongitude());
        assertEquals(latitudeForPosition2, photo2.getGpsPositionLatitude());
        assertEquals("Prague", photo2.getCity());
        assertEquals("Prague", photo2.getRegion());
        assertEquals("Prague", photo2.getLocality());
        assertEquals("Czechia", photo2.getCountry());
        assertEquals("Europe", photo2.getContinent());
    }
}
