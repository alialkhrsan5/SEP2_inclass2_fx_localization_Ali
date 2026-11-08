package app;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ConversionTest {

    @Test
    void constructorForANewConversion() {
        Conversion conversion = new Conversion(32, 2, 1, 0, "fi");

        assertEquals(32, conversion.getInputValue());
        assertEquals(2, conversion.getFromUnitId());
        assertEquals(1, conversion.getToUnitId());
        assertEquals(0, conversion.getResultValue());
        assertEquals("fi", conversion.getLang());
        assertNull(conversion.getCreatedAt());
    }

    @Test
    void constructorForARowReadFromTheDatabase() {
        LocalDateTime now = LocalDateTime.now();
        Conversion conversion = new Conversion(7, 300, 3, 1, 26.85, "ja", now);

        assertEquals(7, conversion.getId());
        assertEquals(300, conversion.getInputValue());
        assertEquals(3, conversion.getFromUnitId());
        assertEquals(1, conversion.getToUnitId());
        assertEquals(26.85, conversion.getResultValue());
        assertEquals("ja", conversion.getLang());
        assertEquals(now, conversion.getCreatedAt());
    }
}
