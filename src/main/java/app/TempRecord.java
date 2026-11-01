package app;

import java.time.LocalDateTime;

public class TempRecord {

    private int id;
    private double inputValue;
    private double resultValue;
    private int temperatureUnitId;
    private LocalDateTime createdAt;

    public TempRecord(double inputValue, double resultValue, int temperatureUnitId) {
        this.inputValue = inputValue;
        this.resultValue = resultValue;
        this.temperatureUnitId = temperatureUnitId;
    }

    public TempRecord(int id, double inputValue, double resultValue,
                      int temperatureUnitId, LocalDateTime createdAt) {
        this.id = id;
        this.inputValue = inputValue;
        this.resultValue = resultValue;
        this.temperatureUnitId = temperatureUnitId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public double getInputValue() {
        return inputValue;
    }

    public double getResultValue() {
        return resultValue;
    }

    public int getTemperatureUnitId() {
        return temperatureUnitId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
