package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TempCalculatorTest {

    @Test
    void fahrenheitToCelsiusConvertsCorrectly() {
        assertEquals(0.0, TempCalculator.fahrenheitToCelsius(32), 0.0001);
        assertEquals(100.0, TempCalculator.fahrenheitToCelsius(212), 0.0001);
        assertEquals(-40.0, TempCalculator.fahrenheitToCelsius(-40), 0.0001);
    }

    @Test
    void celsiusToFahrenheitConvertsCorrectly() {
        assertEquals(32.0, TempCalculator.celsiusToFahrenheit(0), 0.0001);
        assertEquals(212.0, TempCalculator.celsiusToFahrenheit(100), 0.0001);
        assertEquals(98.6, TempCalculator.celsiusToFahrenheit(37), 0.0001);
    }

    @Test
    void kelvinToCelsiusConvertsCorrectly() {
        assertEquals(26.85, TempCalculator.kelvinToCelsius(300), 0.0001);
        assertEquals(0.0, TempCalculator.kelvinToCelsius(273.15), 0.0001);
        assertEquals(-273.15, TempCalculator.kelvinToCelsius(0), 0.0001);
    }

    @Test
    void celsiusToKelvinConvertsCorrectly() {
        assertEquals(273.15, TempCalculator.celsiusToKelvin(0), 0.0001);
        assertEquals(373.15, TempCalculator.celsiusToKelvin(100), 0.0001);
    }

    @Test
    void convertHandlesEveryUnitPair() {
        assertEquals(0.0, TempCalculator.convert(32, "fahrenheit", "celsius"), 0.0001);
        assertEquals(32.0, TempCalculator.convert(0, "celsius", "fahrenheit"), 0.0001);
        assertEquals(26.85, TempCalculator.convert(300, "kelvin", "celsius"), 0.0001);
        assertEquals(273.15, TempCalculator.convert(0, "celsius", "kelvin"), 0.0001);
        assertEquals(212.0, TempCalculator.convert(373.15, "kelvin", "fahrenheit"), 0.0001);
        assertEquals(373.15, TempCalculator.convert(212, "fahrenheit", "kelvin"), 0.0001);
    }

    @Test
    void convertToTheSameUnitReturnsTheSameValue() {
        assertEquals(25.0, TempCalculator.convert(25, "celsius", "celsius"), 0.0001);
        assertEquals(70.0, TempCalculator.convert(70, "fahrenheit", "fahrenheit"), 0.0001);
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
        assertFalse(TempCalculator.isExtremeTemperature(0));
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
    void convertRejectsUnknownUnits() {
        assertThrows(IllegalArgumentException.class,
                () -> TempCalculator.convert(10, "rankine", "celsius"));
        assertThrows(IllegalArgumentException.class,
                () -> TempCalculator.convert(10, "celsius", "rankine"));
    }
}
