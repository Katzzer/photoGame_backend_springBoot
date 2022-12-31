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
    VerifyGPSPosition verifyGPSPosition;

    Position position1 = new Position(50.2092567, 15.8327564,"Hradec Kralove",null, null, null, null);
    Photo testingPhoto1 = new Photo("data:image/jpeg;base64,someValue","123", position1);

    Position position2 = new Position(50.0486575, 14.5111506, "Prague",null, null, null, null);
    Photo testingPhoto2 = new Photo("data:image/jpeg;base64,someValue","123", position2);

    @Test
    void isValidGPSPositionAtEnteredCity() {
        // given

        // when
        boolean validGPSPositionAtEnteredCity1 = verifyGPSPosition.isValidGPSPositionAtEnteredCity(testingPhoto1);
        boolean validGPSPositionAtEnteredCity2 = verifyGPSPosition.isValidGPSPositionAtEnteredCity(testingPhoto2);

        // then
        assertTrue(validGPSPositionAtEnteredCity1);
        assertTrue(validGPSPositionAtEnteredCity2);
    }
}