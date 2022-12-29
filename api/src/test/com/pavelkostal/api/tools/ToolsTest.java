package com.pavelkostal.api.tools;

import com.pavelkostal.api.entity.Photo;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void testReplaceSpaceWithUnderscore() {
        // Given
        String city = "Hradec Kralove";

        // When
        String cityWithUnderscore = Tools.replaceSpaceWithUnderscore(city);

        // Then
        String expectedValue = "Hradec_Kralove";
        assertEquals(cityWithUnderscore, expectedValue);
    }

    @Test
    void testReplaceUnderscoreWithSpace() {
        // Given
        String city1 = "Hradec_Kralove";
        String city2 = "Nove_Mesto";
        Photo photo1 = new Photo("aaa", 15, 15, city1, "123");
        Photo photo2 = new Photo("aaa", 1, 1, city2, "123");
        List<Photo> photos = List.of(photo1, photo2);

        // When
        List<Photo> photosWithSpace = Tools.replaceUnderscoreWithSpace(photos);

        // Then
        String city1WithSpace = "Hradec Kralove";
        String city2WithSpace = "Nove Mesto";
        assertEquals(photosWithSpace.get(0).getCity(), city1WithSpace);
        assertEquals(photosWithSpace.get(1).getCity(), city2WithSpace);
    }
}