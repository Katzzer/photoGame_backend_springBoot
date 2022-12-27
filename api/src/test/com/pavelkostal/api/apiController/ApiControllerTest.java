package com.pavelkostal.api.apiController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.JWTClaimsSet;
import com.pavelkostal.api.entity.Photo;
import com.pavelkostal.api.security.AwsCognitoJwtAuthFilter;
import com.pavelkostal.api.security.JwtAuthentication;
import com.pavelkostal.api.service.PhotoService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.io.IOException;
import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
@AutoConfigureMockMvc
class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PhotoService photoService;
    
    @Autowired
    private WebApplicationContext context;
    
    @MockBean
    private AwsCognitoJwtAuthFilter awsCognitoJwtAuthenticationFilter;
    
    private String idToken = "eyJraWQiOiJcL20zMTh0ZWgzdktMNTlQc0pycHFJU3VhZHJaUXJZSUN2bURFNVF6YWpxRT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyZDBhN2QwMS1jYjk1LTRhZTItODliZC05M2NiN2FjN2I4YmEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LWNlbnRyYWwtMS5hbWF6b25hd3MuY29tXC9ldS1jZW50cmFsLTFfSVczUnh4MXJDIiwiY29nbml0bzp1c2VybmFtZSI6ImthdHp6Iiwib3JpZ2luX2p0aSI6IjQ5M2QyNGIzLTZhMzMtNDkwMC04NmI5LTk0ZGVjMmFlYzRmMyIsImF1ZCI6IjQxMnRxa2NtYWplaXJtZ2N1aGhsYmJxZTNoIiwiZXZlbnRfaWQiOiIzYjA3MzRjNi02YzQzLTRmYzAtOGM4My03OTYzMGExMTY3N2MiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY3MjAzODUzMywibmFtZSI6ImthdHp6IiwiZXhwIjoxNjcyMDgzMDk1LCJpYXQiOjE2NzIwNzk0OTUsImp0aSI6IjgxOTZlODZjLTExOTYtNDYwNS04ZmY4LTBiNjkxMmRiNjY5NiIsImVtYWlsIjoia2F0enpAc2V6bmFtLmN6In0.HvkVFd8WfDYP1Q2HWmEpIkkJaZe-ZXS7bLeT78MypYZJqUX-TUhB8VcMpbJiezkT-Op3LfR5RMVJlQOLGk0D4Ki6HmvVIAgIpgtkwfq5XmNr80l-rrp_rTbBj5HyvpejtoII4lV0WN8sYyG_yfPr2175tZ8jl-owTKkMiZG9d-AOy5N2hRfWjutpZ-DIZjkP7XBQN_D0jjWj3CjA9xmilNuHXwyW0hzf0Xuc19YwJR9cp3DoGHnXQCWM9PxcVMlLeFSg21y6fgGgxpTBSMElOB1snV90TWmmHlcBYSPD4cII7BptAtOOYDIZzQYyFdka1SiIykPFrR71Rvx8KX4YJA";
    
    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).addFilter(awsCognitoJwtAuthenticationFilter).build();
    }
    
    @Test
    @DisplayName("Test Hello Endpoint ")
    @WithMockUser
    void itShouldTesAccessToHelloApiEndpoint() throws Exception {
        // Given
        
        // When
        ResultActions resultActions = mockMvc.perform(get("/api/v1/data"));
        
        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Test saveImage endpoint")
    @WithMockUser
    void itShouldTesSaveImageApiEndpoint() throws Exception {
        // Given
        Photo testingPhoto = new Photo("data:image/jpeg;base64,someValue", 50.2092567, 15.8327564, "aaa");
        ObjectMapper objectMapper = new ObjectMapper();

        // When
        when(photoService.savePhoto(any())).thenReturn(1L);
        ResultActions resultActions = mockMvc.perform(post("/api/v1/data/aaa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testingPhoto)));

        // Then
        resultActions.andExpect(status().is2xxSuccessful());
    }
}