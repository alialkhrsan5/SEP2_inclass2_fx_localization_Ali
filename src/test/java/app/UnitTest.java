package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UnitTest {

    @Test
    void constructorAndGettersWork() {
        Unit unit = new Unit(1, "celsius", "Celsius", "Degrees Celsius");

        assertEquals(1, unit.getId());
        assertEquals("celsius", unit.getCode());
        assertEquals("Celsius", unit.getName());
        assertEquals("Degrees Celsius", unit.getDescription());
    }

    @Test
    void toStringReturnsTheLocalizedName() {
        Unit unit = new Unit(3, "kelvin", "ケルビン", "温度のSI単位");
        assertEquals("ケルビン", unit.toString());
    }
}
