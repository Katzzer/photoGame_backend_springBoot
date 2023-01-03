package com.pavelkostal.api.tools;

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
    
    @Test
    void itShouldCheckIfValidImageWasSent() {
        // Given
        String imageJpeg = "data:image/jpeg;base64,someValue";
        String imageJpg = "data:image/jpg;base64,someValue";
        String imagePng = "data:image/png;base64,someValue";
        String imageInvalid = "someValue,someValue";
        
        // When
        boolean validImageJpeg = Tools.isValidImage(imageJpeg);
        boolean validImageJpg = Tools.isValidImage(imageJpg);
        boolean validImagePng = Tools.isValidImage(imagePng);
        boolean invalidImage = Tools.isValidImage(imageInvalid);
    
        // Then
        assertTrue(validImageJpeg);
        assertTrue(validImageJpg);
        assertTrue(validImagePng);
        assertFalse(invalidImage);
    }

    @Test
    void replaceDiacriticsInString() {
        // Given
        String city = "Hradec Králové";
        String specialCharacters = "ěščřžýáíéúůĚŠČŘŽÝÁÍÉÚ";

        // When
        String cityWithReplacedText = Tools.replaceSpecialCharactersInString(city);
        String replacedSpecialCharacters = Tools.replaceSpecialCharactersInString(specialCharacters);

        // Then
        assertEquals("Hradec Kralove", cityWithReplacedText);
        assertEquals("escrzyaieuuESCRZYAIEU", replacedSpecialCharacters);
    }
}