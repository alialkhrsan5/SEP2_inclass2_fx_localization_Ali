package app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


public class UnitDAOTest {

    private static boolean databaseAvailable;

    private final UnitDAO dao = new UnitDAO();

    @BeforeAll
    static void checkDatabase() {
        try (Connection conn = DBConnection.getConnection()) {
            databaseAvailable = conn != null;
        } catch (SQLException e) {
            databaseAvailable = false;
        }
    }

    private Map<String, Unit> unitsByCode(String lang) throws SQLException {
        return dao.getUnits(lang).stream()
                .collect(Collectors.toMap(Unit::getCode, Function.identity()));
    }

    @Test
    void returnsAllThreeUnits() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        List<Unit> units = dao.getUnits("en");

        assertEquals(3, units.size());
        assertEquals("celsius", units.get(0).getCode());
    }

    @Test
    void returnsEnglishNames() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        Map<String, Unit> units = unitsByCode("en");

        assertEquals("Celsius", units.get("celsius").getName());
        assertEquals("Fahrenheit", units.get("fahrenheit").getName());
        assertEquals("Kelvin", units.get("kelvin").getName());
    }

    @Test
    void returnsJapaneseNames() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        Map<String, Unit> units = unitsByCode("ja");

        assertEquals("摂氏", units.get("celsius").getName());
        assertEquals("華氏", units.get("fahrenheit").getName());
    }

    @Test
    void returnsArabicNames() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        Map<String, Unit> units = unitsByCode("ar");

        assertEquals("مئوية", units.get("celsius").getName());
    }

    @Test
    void namesDifferBetweenLanguages() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        String english = unitsByCode("en").get("kelvin").getDescription();
        String finnish = unitsByCode("fi").get("kelvin").getDescription();

        assertNotEquals(english, finnish);
    }

    @Test
    void fallsBackToEnglishForAnUnsupportedLanguage() throws SQLException {
        assumeTrue(databaseAvailable, "No database connection");

        Map<String, Unit> german = unitsByCode("de");
        Map<String, Unit> english = unitsByCode("en");

        assertEquals(3, german.size());
        for (String code : english.keySet()) {
            assertEquals(english.get(code).getName(), german.get(code).getName());
            assertEquals(english.get(code).getDescription(), german.get(code).getDescription());
        }
    }
}
