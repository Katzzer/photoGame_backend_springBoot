package com.pavelkostal.api.error;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class SomeErrorTest {

    @Test
    public void thisTestShouldFail() {
        assertEquals(true, false);
    }
}
