package app;

import java.time.LocalDateTime;

public class Conversion {

    private int id;
    private final double inputValue;
    private final int fromUnitId;
    private final int toUnitId;
    private final double resultValue;
    private final String lang;
    private LocalDateTime createdAt;

    public Conversion(double inputValue, int fromUnitId, int toUnitId, double resultValue, String lang) {
        this.inputValue = inputValue;
        this.fromUnitId = fromUnitId;
        this.toUnitId = toUnitId;
        this.resultValue = resultValue;
        this.lang = lang;
    }

    public Conversion(int id, double inputValue, int fromUnitId, int toUnitId,
                      double resultValue, String lang, LocalDateTime createdAt) {
        this.id = id;
        this.inputValue = inputValue;
        this.fromUnitId = fromUnitId;
        this.toUnitId = toUnitId;
        this.resultValue = resultValue;
        this.lang = lang;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public double getInputValue() {
        return inputValue;
    }

    public int getFromUnitId() {
        return fromUnitId;
    }

    public int getToUnitId() {
        return toUnitId;
    }

    public double getResultValue() {
        return resultValue;
    }

    public String getLang() {
        return lang;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
