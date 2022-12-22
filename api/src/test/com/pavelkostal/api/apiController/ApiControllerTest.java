package com.pavelkostal.api.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.service.PhotoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PhotoService photoService;

    @BeforeEach
    public void beforeEach() {

    }

    @Test
    @DisplayName("Test saveImage endpoint")
    void itShouldTesSaveImageApiEndpoint() throws Exception {
        // Given
        Photo testingPhoto = new Photo("aabbcc", 50.2092567, 15.8327564, "aaa");
        ObjectMapper objectMapper = new ObjectMapper();

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testingPhoto)));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }
}