package com.pavelkostal.api.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolsTest {

    @Test
    void itShouldTestCorrectGps() {
        // Given
        double latitude =  50.2092567;
        double longitude = 15.8327564;

        // When
        boolean validGps = Tools.isValidGps(latitude, longitude);

        // Then
        assertTrue(validGps);
    }

    @Test
    void itShouldTestIncorrectGps() {
        // Given
        double latitude =  50.2092567;
        double longitude = 190;

        // When
        boolean validGps = Tools.isValidGps(latitude, longitude);

        // Then
        assertFalse(validGps);
    }
}