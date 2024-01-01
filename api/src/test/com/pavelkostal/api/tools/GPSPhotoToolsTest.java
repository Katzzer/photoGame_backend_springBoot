package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GPSPhotoToolsTest {

    @Autowired
    GPSPositionTools underTest;
    
    double latitudeForPosition1 = 50.2092567;
    double longitudeForPosition1 = 15.8327564;
    Photo photo1 = new Photo(latitudeForPosition1, longitudeForPosition1,"Hradec Kralove",null, null, null, null);
    User testingUser1 = new User("123", photo1);

    double latitudeForPosition2 = 50.0486575;
    double longitudeForPosition2 = 14.5111506;
    Photo photo2 = new Photo(latitudeForPosition2, longitudeForPosition2, "Prague",null, null, null, null);
    User testingUser2 = new User("123", photo2);

    // TODO: add test for this
//    double latitudeForPosition3 = 15.8327564;
//    double longitudeForPosition3 = 50.2092567;
//    Position position3 = new Position(latitudeForPosition1, longitudeForPosition1,"Hradec Kralove",null, null, null, null);
//    Photo testingPhoto3 = new Photo("data:image/jpeg;base64,someValue","123", position1);

    @Test
    void isValidGPSPositionAtEnteredCity() {
        // given

        // when
        boolean validGPSPositionAtEnteredCity1 = underTest.isValidGPSPositionAtEnteredCity(testingUser1);
        boolean validGPSPositionAtEnteredCity2 = underTest.isValidGPSPositionAtEnteredCity(testingUser2);

        // then
        assertTrue(validGPSPositionAtEnteredCity1);
        assertTrue(validGPSPositionAtEnteredCity2);
    }

    @Test
    void getPositionFromGps() {
        // Given

        // When
        Photo photoFromGps1 = underTest.getPositionInformationFromGps(testingUser1);
        Photo photoFromGps2 = underTest.getPositionInformationFromGps(testingUser2);

        // Then
        assertEquals(longitudeForPosition1, photoFromGps1.getGpsPositionLongitude());
        assertEquals(latitudeForPosition1, photoFromGps1.getGpsPositionLatitude());
        assertEquals("Hradec Kralove", photoFromGps1.getCity());
        assertEquals("Hradec Králové", photoFromGps1.getRegion());
        assertEquals("Hradec Králové", photoFromGps1.getLocality());
        assertEquals("Czechia", photoFromGps1.getCountry());
        assertEquals("Europe", photoFromGps1.getContinent());

        assertEquals(longitudeForPosition2, photoFromGps2.getGpsPositionLongitude());
        assertEquals(latitudeForPosition2, photoFromGps2.getGpsPositionLatitude());
        assertEquals("Prague", photoFromGps2.getCity());
        assertEquals("Prague", photoFromGps2.getRegion());
        assertEquals("Prague", photoFromGps2.getLocality());
        assertEquals("Czechia", photoFromGps2.getCountry());
        assertEquals("Europe", photoFromGps2.getContinent());
    }
}
