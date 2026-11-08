package app;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController {

    private static final int HISTORY_SIZE = 5;

    private final UnitDAO unitDAO = new UnitDAO();
    private final ConversionDAO conversionDAO = new ConversionDAO();

    @FXML private ComboBox<String> languageComboBox;
    @FXML private ComboBox<Unit> fromUnitComboBox;
    @FXML private ComboBox<Unit> toUnitComboBox;
    @FXML private TextField valueField;
    @FXML private Label resultLabel;
    @FXML private Label descriptionLabel;
    @FXML private TableView<Conversion> tableView;
    @FXML private TableColumn<Conversion, Number> inputColumn;
    @FXML private TableColumn<Conversion, String> fromColumn;
    @FXML private TableColumn<Conversion, String> toColumn;
    @FXML private TableColumn<Conversion, Number> resultColumn;
    @FXML private TableColumn<Conversion, String> langColumn;
    @FXML private TableColumn<Conversion, String> createdColumn;

    private Locale locale;
    private ResourceBundle bundle;

    private final Map<Integer, Unit> unitsById = new HashMap<>();

    public void init(Locale locale, ResourceBundle bundle) {
        this.locale = locale;
        this.bundle = bundle;

        setUpLanguageComboBox();
        setUpTable();
        loadUnits();
        loadHistory();
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

        // the listener is added after selecting, so selecting does not reload the view
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

    /**
     * Unit names come from the database in the selected language.
     */
    private void loadUnits() {
        try {
            List<Unit> units = unitDAO.getUnits(locale.getLanguage());

            unitsById.clear();
            for (Unit unit : units) {
                unitsById.put(unit.getId(), unit);
            }

            fromUnitComboBox.getItems().setAll(units);
            toUnitComboBox.getItems().setAll(units);

            if (units.size() >= 2) {
                fromUnitComboBox.getSelectionModel().select(1);
                toUnitComboBox.getSelectionModel().select(0);
            }

            fromUnitComboBox.setOnAction(event -> showDescription());
            showDescription();

        } catch (SQLException e) {
            showError(bundle.getString("error.loadUnits") + e.getMessage());
        }
    }

    private void showDescription() {
        Unit unit = fromUnitComboBox.getValue();
        descriptionLabel.setText(unit == null ? "" : unit.getDescription());
    }

    private void setUpTable() {
        inputColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getInputValue()));
        fromColumn.setCellValueFactory(data ->
                new SimpleStringProperty(unitName(data.getValue().getFromUnitId())));
        toColumn.setCellValueFactory(data ->
                new SimpleStringProperty(unitName(data.getValue().getToUnitId())));
        resultColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getResultValue()));
        langColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getLang()));
        createdColumn.setCellValueFactory(data ->
                new SimpleStringProperty(String.valueOf(data.getValue().getCreatedAt())));
    }

    private String unitName(int unitId) {
        Unit unit = unitsById.get(unitId);
        return unit == null ? String.valueOf(unitId) : unit.getName();
    }

    private void loadHistory() {
        try {
            tableView.getItems().setAll(conversionDAO.getLatest(HISTORY_SIZE));
        } catch (SQLException e) {
            showError(bundle.getString("error.loadRecords") + e.getMessage());
        }
    }

    @FXML
    private void handleConvert() {
        try {
            double value = Double.parseDouble(valueField.getText().trim());
            Unit from = fromUnitComboBox.getValue();
            Unit to = toUnitComboBox.getValue();

            if (from == null || to == null) {
                showError(bundle.getString("error.selectUnits"));
                return;
            }

            double result = TempCalculator.convert(value, from.getCode(), to.getCode());
            resultLabel.setText(String.format(locale, bundle.getString("result.format"), result, to.getName()));

            conversionDAO.save(new Conversion(value, from.getId(), to.getId(), result, locale.getLanguage()));

            loadHistory();
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
