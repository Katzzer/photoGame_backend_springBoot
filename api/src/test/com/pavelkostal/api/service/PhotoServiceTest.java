package com.pavelkostal.api.service;

import com.pavelkostal.api.constants.ResponseMessages;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhoto;
import com.pavelkostal.api.model.ResponsePhotoSaved;
import com.pavelkostal.api.repository.PhotoRepository;
import com.pavelkostal.api.tools.TokenTool;
import com.pavelkostal.api.tools.Tools;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class PhotoServiceTest {

    @Autowired
    PhotoService underTest;

    @MockBean
    TokenTool tokenTool;

    @MockBean
    Tools tools;

    @Autowired
    PhotoRepository photoRepository;

    MockMultipartFile photoFile;

    private final String idToken = "eyJraWQiOiJcL20zMTh0ZWgzdktMNTlQc0pycHFJU3VhZHJaUXJZSUN2bURFNVF6YWpxRT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyZDBhN2QwMS1jYjk1LTRhZTItODliZC05M2NiN2FjN2I4YmEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LWNlbnRyYWwtMS5hbWF6b25hd3MuY29tXC9ldS1jZW50cmFsLTFfSVczUnh4MXJDIiwiY29nbml0bzp1c2VybmFtZSI6ImthdHp6Iiwib3JpZ2luX2p0aSI6IjQ5M2QyNGIzLTZhMzMtNDkwMC04NmI5LTk0ZGVjMmFlYzRmMyIsImF1ZCI6IjQxMnRxa2NtYWplaXJtZ2N1aGhsYmJxZTNoIiwiZXZlbnRfaWQiOiIzYjA3MzRjNi02YzQzLTRmYzAtOGM4My03OTYzMGExMTY3N2MiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY3MjAzODUzMywibmFtZSI6ImthdHp6IiwiZXhwIjoxNjcyMDgzMDk1LCJpYXQiOjE2NzIwNzk0OTUsImp0aSI6IjgxOTZlODZjLTExOTYtNDYwNS04ZmY4LTBiNjkxMmRiNjY5NiIsImVtYWlsIjoia2F0enpAc2V6bmFtLmN6In0.HvkVFd8WfDYP1Q2HWmEpIkkJaZe-ZXS7bLeT78MypYZJqUX-TUhB8VcMpbJiezkT-Op3LfR5RMVJlQOLGk0D4Ki6HmvVIAgIpgtkwfq5XmNr80l-rrp_rTbBj5HyvpejtoII4lV0WN8sYyG_yfPr2175tZ8jl-owTKkMiZG9d-AOy5N2hRfWjutpZ-DIZjkP7XBQN_D0jjWj3CjA9xmilNuHXwyW0hzf0Xuc19YwJR9cp3DoGHnXQCWM9PxcVMlLeFSg21y6fgGgxpTBSMElOB1snV90TWmmHlcBYSPD4cII7BptAtOOYDIZzQYyFdka1SiIykPFrR71Rvx8KX4YJA";
    private static final String IMAGE_FILE_NAME = "image.jpg";

    @BeforeEach
    public void beforeEach() throws IOException, URISyntaxException {
        BufferedImage photo;
        URL url = getClass().getResource(IMAGE_FILE_NAME);
        File file = Paths.get(url.toURI()).toFile();
        photo = ImageIO.read(file);

        when(tools.toByteArray(any(), any())).thenCallRealMethod();
        when(tools.replaceUnderscoreWithSpaceForString(any())).thenCallRealMethod();
        photoFile = new MockMultipartFile("imageFile", "filename.txt", MediaType.IMAGE_JPEG_VALUE,  tools.toByteArray(photo, "jpg"));
        wipeAllDataInDatabase();
        insertDataToDatabaseForTest();
    }

    @AfterEach
    public void afterEach() {
        wipeAllDataInDatabase();
    }

    @Test
    void savePhoto() {
        // Given
        Photo newPhoto = new Photo("123", 50.20923, 15.83277, "Hradec Kralove", null, null, "Czech republic", null);

        // When
        when(tokenTool.getUniqueUserId(any())).thenReturn("123");
        when(tools.replaceSpecialCharactersInString(any())).thenCallRealMethod();
        ResponseEntity<ResponsePhoto> responseSavedPhoto = underTest.savePhoto(idToken , photoFile, newPhoto);

        // Then
        responseSavedPhoto.getStatusCode().is2xxSuccessful();
        ResponsePhotoSaved body = (ResponsePhotoSaved) responseSavedPhoto.getBody();
        assertNotNull(body);
        assertEquals(ResponseMessages.PHOTO_SAVED.toString(), body.message());
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
        String uniqueId = "123";

        // When
        when(tokenTool.getUniqueUserId(any())).thenReturn(uniqueId);
        ResponseEntity<List<Photo>> response = underTest.getAllPhotosForSelectedUser("fakeValue");

        // Then
        List<Photo> responseBodyListOfPhotos = response.getBody();
        assertNotNull(responseBodyListOfPhotos);
        assertEquals(3, responseBodyListOfPhotos.size());
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

    private void insertDataToDatabaseForTest() {
        Photo photo1FromPrague = new Photo("123", 50.073658, 14.418540, "Prague", null, null, "Czech republic", null);
        Photo photo2FromPrague = new Photo("123", 50.073658, 14.418540, "Prague", null, null, "Czech republic", null);
        Photo photo3FromHk = new Photo("123", 50.20923, 15.83277, "Hradec Kralove", null, null, "Czech republic", null);
        List<Photo> listOfPhotos = List.of(photo1FromPrague, photo2FromPrague, photo3FromHk);

        photoRepository.saveAll(listOfPhotos);
    }

    private void wipeAllDataInDatabase() {
        photoRepository.deleteAll();
    }
}
