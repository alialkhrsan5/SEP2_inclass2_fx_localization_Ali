package app;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TempRecordTest {

    @Test
    void constructorWithoutIdAndTimestamp() {
        TempRecord record = new TempRecord(32, 0, 1);

        assertEquals(32, record.getInputValue());
        assertEquals(0, record.getResultValue());
        assertEquals(1, record.getTemperatureUnitId());
        assertNull(record.getCreatedAt());
    }

    @Test
    void constructorWithIdAndTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        TempRecord record = new TempRecord(5, 300, 26.85, 3, now);

        assertEquals(5, record.getId());
        assertEquals(300, record.getInputValue());
        assertEquals(26.85, record.getResultValue());
        assertEquals(3, record.getTemperatureUnitId());
        assertEquals(now, record.getCreatedAt());
    }
}
