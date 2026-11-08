package app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class ConversionDAOTest {

    private static boolean databaseAvailable;

    private final ConversionDAO dao = new ConversionDAO();

    @BeforeAll
    static void checkDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            databaseAvailable = conn != null;
        } catch (SQLException e) {
            databaseAvailable = false;
        }
    }

    @Test
    void savedConversionIsReturnedAsTheNewest() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        double marker = System.currentTimeMillis() % 1000;
        dao.save(new Conversion(marker, 2, 1, TempCalculator.convert(marker, "fahrenheit", "celsius"), "en"));

        List<Conversion> latest = dao.getLatest(5);

        assertFalse(latest.isEmpty());
        assertEquals(marker, latest.get(0).getInputValue(), 0.0001);
        assertEquals(2, latest.get(0).getFromUnitId());
        assertEquals(1, latest.get(0).getToUnitId());
        assertEquals("en", latest.get(0).getLang());
        assertNotNull(latest.get(0).getCreatedAt());
    }

    @Test
    void getLatestNeverReturnsMoreRowsThanAsked() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        assertTrue(dao.getLatest(5).size() <= 5);
    }
}
