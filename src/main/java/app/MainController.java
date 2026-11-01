package app;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainController {

    private final TemperatureUnitDAO unitDAO = new TemperatureUnitDAO();
    private final TempRecordDAO recordDAO = new TempRecordDAO();

    @FXML private ComboBox<String> languageComboBox;
    @FXML private ComboBox<TemperatureUnit> unitComboBox;
    @FXML private TextField valueField;
    @FXML private Label resultLabel;
    @FXML private TableView<TempRecord> tableView;
    @FXML private TableColumn<TempRecord, Number> idColumn;
    @FXML private TableColumn<TempRecord, Number> inputColumn;
    @FXML private TableColumn<TempRecord, Number> resultColumn;
    @FXML private TableColumn<TempRecord, String> unitColumn;
    @FXML private TableColumn<TempRecord, String> createdColumn;

    private Locale locale;
    private ResourceBundle bundle;

    public void init(Locale locale, ResourceBundle bundle) {
        this.locale = locale;
        this.bundle = bundle;

        setUpLanguageComboBox();
        setUpUnitComboBox();
        setUpTable();
        loadUnits();
        loadRecords();
    }

    private void setUpLanguageComboBox() {
        languageComboBox.getItems().setAll(Main.LANGUAGE_NAMES);

        int current = 0;
        for (int i = 0; i < Main.LOCALES.length; i++) {
            if (Main.LOCALES[i].equals(locale)) {
                current = i;
            }
        }
        languageComboBox.getSelectionModel().select(current);

        // listener added after selecting, so selecting does not trigger a reload
        languageComboBox.setOnAction(event -> {
            int index = languageComboBox.getSelectionModel().getSelectedIndex();
            if (index >= 0 && !Main.LOCALES[index].equals(locale)) {
                try {
                    Main.showView(Main.LOCALES[index]);
                } catch (Exception e) {
                    showError(e.getMessage());
                }
            }
        });
    }

    private void setUpUnitComboBox() {
        unitComboBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(TemperatureUnit unit, boolean empty) {
                super.updateItem(unit, empty);
                setText(empty || unit == null ? null : localizedUnitName(unit));
            }
        });
        unitComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(TemperatureUnit unit, boolean empty) {
                super.updateItem(unit, empty);
                setText(empty || unit == null ? null : localizedUnitName(unit));
            }
        });
    }

    private String localizedUnitName(TemperatureUnit unit) {
        String key = "unit." + unit.getId();
        return bundle.containsKey(key) ? bundle.getString(key) : unit.getUnitName();
    }

    private void setUpTable() {
        idColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getId()));
        inputColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getInputValue()));
        resultColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getResultValue()));
        unitColumn.setCellValueFactory(data ->
                new SimpleStringProperty(bundle.getString("unit." + data.getValue().getTemperatureUnitId())));
        createdColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCreatedAt())));
    }

    private void loadUnits() {
        try {
            unitComboBox.getItems().setAll(unitDAO.getAllUnits());
            if (!unitComboBox.getItems().isEmpty()) {
                unitComboBox.getSelectionModel().selectFirst();
            }
        } catch (SQLException e) {
            showError(bundle.getString("error.loadUnits") + e.getMessage());
        }
    }

    private void loadRecords() {
        try {
            tableView.getItems().setAll(recordDAO.getAllRecords());
        } catch (SQLException e) {
            showError(bundle.getString("error.loadRecords") + e.getMessage());
        }
    }

    @FXML
    private void handleConvert() {
        try {
            double value = Double.parseDouble(valueField.getText().trim());
            TemperatureUnit selected = unitComboBox.getValue();

            if (selected == null) {
                showError(bundle.getString("error.selectConversion"));
                return;
            }

            double result = TempCalculator.convert(selected.getId(), value);
            resultLabel.setText(String.format(locale, bundle.getString("result.format"), result));

            recordDAO.save(new TempRecord(value, result, selected.getId()));

            loadRecords();
            valueField.clear();

        } catch (NumberFormatException e) {
            showError(bundle.getString("error.number"));
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (SQLException e) {
            showError(bundle.getString("error.database") + e.getMessage());
        }
    }

    private void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
