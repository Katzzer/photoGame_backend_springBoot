package com.pavelkostal.api.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.entity.Position;
import com.pavelkostal.api.service.PhotoService;
import com.pavelkostal.api.tools.TokenTool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // to remove custom filter in Spring Security
@ContextConfiguration
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PhotoService photoService;

    @MockBean
    TokenTool tokenTool;

    Position position1;
    Photo testingPhoto1;

    @BeforeEach
    public void beforeEach() {
        position1 = new Position(50.2092567, 15.8327564,"Hradec Kralove",null, null, null, null);
        testingPhoto1 = new Photo("data:image/jpeg;base64,someValue","123", position1);
    }

    private final String idToken = "eyJraWQiOiJcL20zMTh0ZWgzdktMNTlQc0pycHFJU3VhZHJaUXJZSUN2bURFNVF6YWpxRT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyZDBhN2QwMS1jYjk1LTRhZTItODliZC05M2NiN2FjN2I4YmEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LWNlbnRyYWwtMS5hbWF6b25hd3MuY29tXC9ldS1jZW50cmFsLTFfSVczUnh4MXJDIiwiY29nbml0bzp1c2VybmFtZSI6ImthdHp6Iiwib3JpZ2luX2p0aSI6IjQ5M2QyNGIzLTZhMzMtNDkwMC04NmI5LTk0ZGVjMmFlYzRmMyIsImF1ZCI6IjQxMnRxa2NtYWplaXJtZ2N1aGhsYmJxZTNoIiwiZXZlbnRfaWQiOiIzYjA3MzRjNi02YzQzLTRmYzAtOGM4My03OTYzMGExMTY3N2MiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY3MjAzODUzMywibmFtZSI6ImthdHp6IiwiZXhwIjoxNjcyMDgzMDk1LCJpYXQiOjE2NzIwNzk0OTUsImp0aSI6IjgxOTZlODZjLTExOTYtNDYwNS04ZmY4LTBiNjkxMmRiNjY5NiIsImVtYWlsIjoia2F0enpAc2V6bmFtLmN6In0.HvkVFd8WfDYP1Q2HWmEpIkkJaZe-ZXS7bLeT78MypYZJqUX-TUhB8VcMpbJiezkT-Op3LfR5RMVJlQOLGk0D4Ki6HmvVIAgIpgtkwfq5XmNr80l-rrp_rTbBj5HyvpejtoII4lV0WN8sYyG_yfPr2175tZ8jl-owTKkMiZG9d-AOy5N2hRfWjutpZ-DIZjkP7XBQN_D0jjWj3CjA9xmilNuHXwyW0hzf0Xuc19YwJR9cp3DoGHnXQCWM9PxcVMlLeFSg21y6fgGgxpTBSMElOB1snV90TWmmHlcBYSPD4cII7BptAtOOYDIZzQYyFdka1SiIykPFrR71Rvx8KX4YJA";


    @Test
    @DisplayName("Test saveImage endpoint")
    @WithMockUser
    void itShouldTesSaveImageApiEndpoint() throws Exception {
        // Given

        ObjectMapper objectMapper = new ObjectMapper();

        // When
        when(tokenTool.getUniqueUserId(any())).thenReturn("12345");

        ResultActions resultActions = mockMvc.perform(post("/api/v1/data")
                        .header("Authorization", "Bearer " + idToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testingPhoto1)));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test saveImage endpoint with invalid GPS")
    @WithMockUser
    void itShouldTesSaveImageApiEndpointWithInvalidGps() throws Exception {
        // Given
        testingPhoto1.getPosition().setGpsPositionLatitude(500000);

        ObjectMapper objectMapper = new ObjectMapper();

        // When
        when(tokenTool.getUniqueUserId(any())).thenReturn("12345");

        ResultActions resultActions = mockMvc.perform(post("/api/v1/data")
                .header("Authorization", "Bearer " + idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testingPhoto1)));

        // Then
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Test saveImage endpoint with invalid GPS")
    @WithMockUser
    void itShouldTesSaveImageApiEndpointWithInvalidPhoto() throws Exception {
        // Given
        testingPhoto1.setPhotoAsString("someValue");
        ObjectMapper objectMapper = new ObjectMapper();

        // When
        when(tokenTool.getUniqueUserId(any())).thenReturn("12345");

        ResultActions resultActions = mockMvc.perform(post("/api/v1/data")
                .header("Authorization", "Bearer " + idToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testingPhoto1)));

        // Then
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Test getImage by ID endpoint")
    @WithMockUser
    void itShouldTestGetImageEndPoint() throws Exception {
        // Given
        long id = 1;

        // When
        when(photoService.getPhotoById(1L)).thenReturn(Optional.of(testingPhoto1));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/data/image/" + id)
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test getImage by ID endpoint when photo does not exist in DB")
    @WithMockUser
    void itShouldTestGetImageEndPointWithInvalidPhotoId() throws Exception {
        long id = 1;

        // When
        when(photoService.getPhotoById(1L)).thenReturn(Optional.empty());

        ResultActions resultActions = mockMvc.perform(get("/api/v1/data/image/" + id)
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Test images ID endpoint for current user ")
    @WithMockUser
    void itShouldTestGetImagesEndPoint() throws Exception {
        Position position2 = new Position(50.2092567, 15.8327564,"Nove Mesto",null, null, null, null);
        Photo testingPhoto2 = new Photo("data:image/jpeg;base64,someValue","123", position2);

        // When
        when(photoService.getAllImagesForSelectedUser(any())).thenReturn(List.of(testingPhoto1, testingPhoto2));

        ResultActions resultActions = mockMvc.perform(get("/api/v1/data/images")
                .header("Authorization", "Bearer " + idToken));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }
}