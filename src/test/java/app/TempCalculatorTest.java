package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TempCalculatorTest {

    @Test
    void fahrenheitToCelsiusConvertsCorrectly() {
        assertEquals(0.0, TempCalculator.fahrenheitToCelsius(32), 0.0001);
        assertEquals(100.0, TempCalculator.fahrenheitToCelsius(212), 0.0001);
        assertEquals(-40.0, TempCalculator.fahrenheitToCelsius(-40), 0.0001);
        assertEquals(37.0, TempCalculator.fahrenheitToCelsius(98.6), 0.0001);
    }

    @Test
    void celsiusToFahrenheitConvertsCorrectly() {
        assertEquals(32.0, TempCalculator.celsiusToFahrenheit(0), 0.0001);
        assertEquals(212.0, TempCalculator.celsiusToFahrenheit(100), 0.0001);
        assertEquals(-40.0, TempCalculator.celsiusToFahrenheit(-40), 0.0001);
        assertEquals(98.6, TempCalculator.celsiusToFahrenheit(37), 0.0001);
    }

    @Test
    void kelvinToCelsiusConvertsCorrectly() {
        assertEquals(26.85, TempCalculator.kelvinToCelsius(300), 0.0001);
        assertEquals(0.0, TempCalculator.kelvinToCelsius(273.15), 0.0001);
        assertEquals(-273.15, TempCalculator.kelvinToCelsius(0), 0.0001);
    }

    @Test
    void extremeTemperaturesAreDetected() {
        assertTrue(TempCalculator.isExtremeTemperature(-41));
        assertTrue(TempCalculator.isExtremeTemperature(51));
    }

    @Test
    void boundaryValuesAreNotExtreme() {
        assertFalse(TempCalculator.isExtremeTemperature(-40));
        assertFalse(TempCalculator.isExtremeTemperature(50));
    }

    @Test
    void normalTemperaturesAreNotExtreme() {
        assertFalse(TempCalculator.isExtremeTemperature(0));
        assertFalse(TempCalculator.isExtremeTemperature(25));
    }

    @Test
    void validateCelsiusRejectsBelowAbsoluteZero() {
        assertThrows(IllegalArgumentException.class, () -> TempCalculator.validateCelsius(-300));
    }

    @Test
    void validateKelvinRejectsNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> TempCalculator.validateKelvin(-1));
    }

    @Test
    void convertUsesTheConversionIdFromTheDatabase() {
        assertEquals(0.0, TempCalculator.convert(1, 32), 0.0001);
        assertEquals(32.0, TempCalculator.convert(2, 0), 0.0001);
        assertEquals(26.85, TempCalculator.convert(3, 300), 0.0001);
    }

    @Test
    void convertRejectsUnknownConversionId() {
        assertThrows(IllegalArgumentException.class, () -> TempCalculator.convert(9, 10));
    }
}
