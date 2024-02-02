package com.pavelkostal.api.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkostal.api.constants.ResponseMessages;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.model.ResponsePhotoSaved;
import com.pavelkostal.api.service.PhotoService;
import com.pavelkostal.api.tools.TokenTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // to remove custom filter in Spring Security
@ContextConfiguration
@ActiveProfiles("test")
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PhotoService photoService;

    @MockBean
    TokenTool tokenTool;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Value("${base-controller-for-test.path}")
    private String serverUrl;

    Photo photo1;
    ObjectMapper objectMapper;
    MockMultipartFile imageFile;
    MockMultipartFile photoAsJson;

    private static final String IMAGE_FILE_NAME = "image.jpg";

    @BeforeEach
    public void beforeEach() throws IOException, URISyntaxException {
        BufferedImage image;
        URL url = getClass().getResource(IMAGE_FILE_NAME);
        File file = Paths.get(url.toURI()).toFile();
        image = ImageIO.read(file);

        photo1 = new Photo("123", 50.2092567, 15.8327564,"Hradec Kralove",null, null, null, null);
        imageFile = new MockMultipartFile("imageFile", "filename.txt", MediaType.IMAGE_JPEG_VALUE,  toByteArray(image, "jpg"));
        objectMapper = new ObjectMapper();
        photoAsJson = new MockMultipartFile("photo", "photo", "application/json", objectMapper.writeValueAsString(photo1).getBytes());
    }

    private final String idToken = "eyJraWQiOiJcL20zMTh0ZWgzdktMNTlQc0pycHFJU3VhZHJaUXJZSUN2bURFNVF6YWpxRT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyZDBhN2QwMS1jYjk1LTRhZTItODliZC05M2NiN2FjN2I4YmEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LWNlbnRyYWwtMS5hbWF6b25hd3MuY29tXC9ldS1jZW50cmFsLTFfSVczUnh4MXJDIiwiY29nbml0bzp1c2VybmFtZSI6ImthdHp6Iiwib3JpZ2luX2p0aSI6IjQ5M2QyNGIzLTZhMzMtNDkwMC04NmI5LTk0ZGVjMmFlYzRmMyIsImF1ZCI6IjQxMnRxa2NtYWplaXJtZ2N1aGhsYmJxZTNoIiwiZXZlbnRfaWQiOiIzYjA3MzRjNi02YzQzLTRmYzAtOGM4My03OTYzMGExMTY3N2MiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY3MjAzODUzMywibmFtZSI6ImthdHp6IiwiZXhwIjoxNjcyMDgzMDk1LCJpYXQiOjE2NzIwNzk0OTUsImp0aSI6IjgxOTZlODZjLTExOTYtNDYwNS04ZmY4LTBiNjkxMmRiNjY5NiIsImVtYWlsIjoia2F0enpAc2V6bmFtLmN6In0.HvkVFd8WfDYP1Q2HWmEpIkkJaZe-ZXS7bLeT78MypYZJqUX-TUhB8VcMpbJiezkT-Op3LfR5RMVJlQOLGk0D4Ki6HmvVIAgIpgtkwfq5XmNr80l-rrp_rTbBj5HyvpejtoII4lV0WN8sYyG_yfPr2175tZ8jl-owTKkMiZG9d-AOy5N2hRfWjutpZ-DIZjkP7XBQN_D0jjWj3CjA9xmilNuHXwyW0hzf0Xuc19YwJR9cp3DoGHnXQCWM9PxcVMlLeFSg21y6fgGgxpTBSMElOB1snV90TWmmHlcBYSPD4cII7BptAtOOYDIZzQYyFdka1SiIykPFrR71Rvx8KX4YJA";


    @Test
    @DisplayName("Test save photo endpoint")
    @WithMockUser
    void itShouldTestSavePhoto() throws Exception {
        // Given
        // When
        ResponsePhotoSaved response = new ResponsePhotoSaved(999L, ResponseMessages.PHOTO_SAVED.toString());
        when(photoService.savePhoto(any(), any(), any())).thenReturn(new ResponseEntity<>(response, HttpStatus.ACCEPTED));

        // Then
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.multipart(serverUrl + "/save-photo")
                        .file(imageFile)
                        .file(photoAsJson)
                        .header("Authorization", "Bearer " + idToken)
                        .param("some-random", "4"))
                .andExpect(status().is(202));
    }

    @Test
    @DisplayName("Test get photo by ID endpoint")
    @WithMockUser
    void itShouldTestGetPhotoEndPoint() throws Exception {
        // Given
        long id = 1;

        // When
        byte[] body = new byte[0];
        when(photoService.getPhotoById(1L, false)).thenReturn(
        ResponseEntity
                .ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .body(body));

        MvcResult result = mockMvc.perform(get(serverUrl + "/photo/" + id)
                        .header("Authorization", "Bearer " + idToken))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Then
        String contentType = result.getResponse().getContentType();
        assertEquals("application/octet-stream", contentType);
    }

    @Test
    @DisplayName("Test images ID endpoint for current user ")
    @WithMockUser
    void itShouldTestGetImagesEndPoint() throws Exception {
        Photo photo2 = new Photo("123", 50.2092567, 15.8327564, "Nove Mesto",null, null, null, null);
        List<Photo> allImagesForUser = List.of(photo1, photo2);

        // When
        byte[] body = new byte[0]; // TODO: remove duplicate code
        when(photoService.getPhotoById(1L, false)).thenReturn(
                ResponseEntity
                        .ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                        .body(body));

        ResultActions resultActions = mockMvc.perform(get(serverUrl + "/photos/all-photos-for-current-user")
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test get photo by ID endpoint")
    @WithMockUser
    void itShouldTestGetPhotoById() throws Exception {
        // Given
        // When
        byte[] body = new byte[0]; // TODO: remove duplicate code
        when(photoService.getPhotoById(1L, false)).thenReturn(
                ResponseEntity
                        .ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                        .body(body));

        ResultActions resultActions = mockMvc.perform(get(serverUrl + "/photo/1")
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test get photo thumbnail by ID endpoint")
    @WithMockUser
    void itShouldTestGetPhotoThumbnailById() throws Exception {
        // Given
        // When
        byte[] body = new byte[0]; // TODO: remove duplicate code
        when(photoService.getPhotoById(1L, false)).thenReturn(
                ResponseEntity
                        .ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                        .body(body));

        ResultActions resultActions = mockMvc.perform(get(serverUrl + "/photo/thumbnail/1")
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test get photos by city")
    @WithMockUser
    void itShouldTestGetListOfPhotosByCity() throws Exception {
        // Given
        // When
        byte[] body = new byte[0]; // TODO: remove duplicate code
        when(photoService.getPhotoById(1L, false)).thenReturn(
                ResponseEntity
                        .ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                        .body(body));

        ResultActions resultActions = mockMvc.perform(get(serverUrl + "/photos/Prague")
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test get photos by city")
    @WithMockUser
    void itShouldTestListCities() throws Exception {
        // Given
        List<String> listOfCities = List.of("Prague", "Brno");

        // When
        byte[] body = new byte[0]; // TODO: remove duplicate code
        when(photoService.getAllCityInDb()).thenReturn(
                ResponseEntity
                        .ok()
                        .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                        .body(listOfCities));

        MvcResult result = mockMvc.perform(get(serverUrl + "/list-of-cities")
                        .header("Authorization", "Bearer " + idToken))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        // Then
        String content = result.getResponse().getContentAsString();

        for (String city : listOfCities) {
            assertTrue(content.contains(city));
        }
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Test that every endpoint is secured")
    void itShouldTestThatEveryEndpointIsSecured() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post(serverUrl + "/save-photo")).andDo(print()).andExpect(status().is4xxClientError());
        this.mockMvc.perform(MockMvcRequestBuilders.post(serverUrl + "/photo/1")).andDo(print()).andExpect(status().is4xxClientError());
        this.mockMvc.perform(MockMvcRequestBuilders.post(serverUrl + "/photo/thumbnail/1")).andDo(print()).andExpect(status().is4xxClientError());
        this.mockMvc.perform(MockMvcRequestBuilders.post(serverUrl + "/photos/city")).andDo(print()).andExpect(status().is4xxClientError());
        this.mockMvc.perform(MockMvcRequestBuilders.post(serverUrl + "/photos/all-photos-for-current-user")).andDo(print()).andExpect(status().is4xxClientError());
        this.mockMvc.perform(MockMvcRequestBuilders.post(serverUrl + "/list-of-cities")).andDo(print()).andExpect(status().is4xxClientError());
    }

    public static byte[] toByteArray(BufferedImage bi, String format) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bi, format, baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return baos.toByteArray();

    }
}
