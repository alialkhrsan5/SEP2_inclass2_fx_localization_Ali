package app;

public class TempCalculator {

    public static final String CELSIUS = "celsius";
    public static final String FAHRENHEIT = "fahrenheit";
    public static final String KELVIN = "kelvin";

    public static void validateCelsius(double celsius) {
        if (celsius < -273.15) {
            throw new IllegalArgumentException("Celsius cannot be below absolute zero: " + celsius);
        }
    }

    public static void validateKelvin(double kelvin) {
        if (kelvin < 0) {
            throw new IllegalArgumentException("Kelvin cannot be negative: " + kelvin);
        }
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9;
    }

    public static double celsiusToFahrenheit(double celsius) {
        validateCelsius(celsius);
        return (celsius * 9 / 5) + 32;
    }

    public static double kelvinToCelsius(double kelvin) {
        validateKelvin(kelvin);
        return kelvin - 273.15;
    }

    public static double celsiusToKelvin(double celsius) {
        validateCelsius(celsius);
        return celsius + 273.15;
    }

    public static boolean isExtremeTemperature(double celsius) {
        return celsius < -40 || celsius > 50;
    }

    public static double toCelsius(double value, String fromCode) {
        return switch (fromCode) {
            case CELSIUS -> {
                validateCelsius(value);
                yield value;
            }
            case FAHRENHEIT -> fahrenheitToCelsius(value);
            case KELVIN -> kelvinToCelsius(value);
            default -> throw new IllegalArgumentException("Unknown unit: " + fromCode);
        };
    }

    public static double fromCelsius(double celsius, String toCode) {
        return switch (toCode) {
            case CELSIUS -> celsius;
            case FAHRENHEIT -> celsiusToFahrenheit(celsius);
            case KELVIN -> celsiusToKelvin(celsius);
            default -> throw new IllegalArgumentException("Unknown unit: " + toCode);
        };
    }

    public static double convert(double value, String fromCode, String toCode) {
        return fromCelsius(toCelsius(value, fromCode), toCode);
    }
}
