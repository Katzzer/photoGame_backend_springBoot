package com.pavelkostal.api.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ToolsTest {

    @Autowired
    Tools underTest;

    @Test
    void replaceDiacriticsInString() {
        // Given
        String city = "Hradec Králové";
        String specialCharacters = "ěščřžýáíéúůĚŠČŘŽÝÁÍÉÚ";

        // When
        String cityWithReplacedText = underTest.replaceSpecialCharactersInString(city);
        String replacedSpecialCharacters = underTest.replaceSpecialCharactersInString(specialCharacters);

        // Then
        assertEquals("Hradec Kralove", cityWithReplacedText);
        assertEquals("escrzyaieuuESCRZYAIEU", replacedSpecialCharacters);
    }
}
