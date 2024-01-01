package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class GPSPhotoToolsTest {

    @Autowired
    GPSPositionTools underTest;
    
    double latitudeForPosition1 = 50.2092567;
    double longitudeForPosition1 = 15.8327564;
    Photo photo1 = new Photo("1", latitudeForPosition1, longitudeForPosition1,"Hradec Kralove",null, null, null, null);

    double latitudeForPosition2 = 50.0486575;
    double longitudeForPosition2 = 14.5111506;
    Photo photo2 = new Photo("2", latitudeForPosition2, longitudeForPosition2, "Prague",null, null, null, null);

    @Test
    void isValidGPSPositionAtEnteredCity() {
        // given

        // when
        boolean validGPSPositionAtEnteredCity1 = underTest.isValidGPSPositionAtEnteredCity(photo1);
        boolean validGPSPositionAtEnteredCity2 = underTest.isValidGPSPositionAtEnteredCity(photo2);

        // then
        assertTrue(validGPSPositionAtEnteredCity1);
        assertTrue(validGPSPositionAtEnteredCity2);
    }

    @Test
    void getPositionFromGps() {
        // Given

        // When
        underTest.setPositionInformationFromGpsToCurrentPhoto(photo1);
        underTest.setPositionInformationFromGpsToCurrentPhoto(photo2);

        // Then
        assertEquals(longitudeForPosition1, photo1.getGpsPositionLongitude());
        assertEquals(latitudeForPosition1, photo1.getGpsPositionLatitude());
        assertEquals("Hradec Kralove", photo1.getCity());
        assertEquals("Hradec Králové", photo1.getRegion());
        assertEquals("Hradec Králové", photo1.getLocality());
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
