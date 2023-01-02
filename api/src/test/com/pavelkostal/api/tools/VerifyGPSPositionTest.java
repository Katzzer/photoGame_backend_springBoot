package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VerifyGPSPositionTest {

    @Autowired
    VerifyGPSPosition underTest;
    
    double latitudeForPosition1 = 50.2092567;
    double longitudeForPosition1 = 15.8327564;
    Position position1 = new Position(latitudeForPosition1, longitudeForPosition1,"Hradec Kralove",null, null, null, null);
    Photo testingPhoto1 = new Photo("data:image/jpeg;base64,someValue","123", position1);

    double latitudeForPosition2 = 50.0486575;
    double longitudeForPosition2 = 14.5111506;
    Position position2 = new Position(latitudeForPosition2, longitudeForPosition2, "Prague",null, null, null, null);
    Photo testingPhoto2 = new Photo("data:image/jpeg;base64,someValue","123", position2);

    // TODO: add test for this
//    double latitudeForPosition3 = 15.8327564;
//    double longitudeForPosition3 = 50.2092567;
//    Position position3 = new Position(latitudeForPosition1, longitudeForPosition1,"Hradec Kralove",null, null, null, null);
//    Photo testingPhoto3 = new Photo("data:image/jpeg;base64,someValue","123", position1);

    @Test
    void isValidGPSPositionAtEnteredCity() {
        // given

        // when
        boolean validGPSPositionAtEnteredCity1 = underTest.isValidGPSPositionAtEnteredCity(testingPhoto1);
        boolean validGPSPositionAtEnteredCity2 = underTest.isValidGPSPositionAtEnteredCity(testingPhoto2);

        // then
        assertTrue(validGPSPositionAtEnteredCity1);
        assertTrue(validGPSPositionAtEnteredCity2);
    }

    @Test
    void getPositionFromGps() {
        // Given

        // When
        Position positionFromGps1 = underTest.getPositionFromGps(testingPhoto1);
        Position positionFromGps2 = underTest.getPositionFromGps(testingPhoto2);

        // Then
        assertEquals(longitudeForPosition1, positionFromGps1.getGpsPositionLongitude());
        assertEquals(latitudeForPosition1, positionFromGps1.getGpsPositionLatitude());
        assertEquals("Hradec Kralove", positionFromGps1.getCity());
        assertEquals("Hradec Králové", positionFromGps1.getRegion());
        assertEquals("Hradec Králové", positionFromGps1.getLocality());
        assertEquals("Czechia", positionFromGps1.getCountry());
        assertEquals("Europe", positionFromGps1.getContinent());

        assertEquals(longitudeForPosition2, positionFromGps2.getGpsPositionLongitude());
        assertEquals(latitudeForPosition2, positionFromGps2.getGpsPositionLatitude());
        assertEquals("Prague", positionFromGps2.getCity());
        assertEquals("Prague", positionFromGps2.getRegion());
        assertEquals("Prague", positionFromGps2.getLocality());
        assertEquals("Czechia", positionFromGps2.getCountry());
        assertEquals("Europe", positionFromGps2.getContinent());
    }
}