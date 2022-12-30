package com.pavelkostal.api.security;

import com.pavelkostal.api.externalApiCalls.PositionStack;
import com.pavelkostal.api.model.PositionStackResponseData;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AwsCognitoIdTokenProcessorTest {

    @Autowired
    AwsCognitoIdTokenProcessor underTest;

    @Autowired
    PositionStack positionStack;

    private final String idToken = "eyJraWQiOiJcL20zMTh0ZWgzdktMNTlQc0pycHFJU3VhZHJaUXJZSUN2bURFNVF6YWpxRT0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIyZDBhN2QwMS1jYjk1LTRhZTItODliZC05M2NiN2FjN2I4YmEiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LWNlbnRyYWwtMS5hbWF6b25hd3MuY29tXC9ldS1jZW50cmFsLTFfSVczUnh4MXJDIiwiY29nbml0bzp1c2VybmFtZSI6ImthdHp6Iiwib3JpZ2luX2p0aSI6IjQ5M2QyNGIzLTZhMzMtNDkwMC04NmI5LTk0ZGVjMmFlYzRmMyIsImF1ZCI6IjQxMnRxa2NtYWplaXJtZ2N1aGhsYmJxZTNoIiwiZXZlbnRfaWQiOiIzYjA3MzRjNi02YzQzLTRmYzAtOGM4My03OTYzMGExMTY3N2MiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTY3MjAzODUzMywibmFtZSI6ImthdHp6IiwiZXhwIjoxNjcyMDgzMDk1LCJpYXQiOjE2NzIwNzk0OTUsImp0aSI6IjgxOTZlODZjLTExOTYtNDYwNS04ZmY4LTBiNjkxMmRiNjY5NiIsImVtYWlsIjoia2F0enpAc2V6bmFtLmN6In0.HvkVFd8WfDYP1Q2HWmEpIkkJaZe-ZXS7bLeT78MypYZJqUX-TUhB8VcMpbJiezkT-Op3LfR5RMVJlQOLGk0D4Ki6HmvVIAgIpgtkwfq5XmNr80l-rrp_rTbBj5HyvpejtoII4lV0WN8sYyG_yfPr2175tZ8jl-owTKkMiZG9d-AOy5N2hRfWjutpZ-DIZjkP7XBQN_D0jjWj3CjA9xmilNuHXwyW0hzf0Xuc19YwJR9cp3DoGHnXQCWM9PxcVMlLeFSg21y6fgGgxpTBSMElOB1snV90TWmmHlcBYSPD4cII7BptAtOOYDIZzQYyFdka1SiIykPFrR71Rvx8KX4YJA";

    @Value("${position.stack.access.key}")
    String positionStackAccessKey;

    @Test
    @Disabled
    void authenticate() throws Exception {

        // TODO: idToken expired, don´t know how to test this

        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + idToken);
        Authentication authenticate = underTest.authenticate(request);

        // when

        // then
        assertEquals(authenticate, null);

    }

    @Test
    void testPositionStack() {
//        TODO: delete this test
        PositionStackResponseData data = positionStack.getData(null, "Prag");

        assertNotNull(data);
    }


}