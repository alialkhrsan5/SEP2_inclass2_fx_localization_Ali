package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static final Locale[] LOCALES = {
            Locale.of("en", "US"),
            Locale.of("fi", "FI"),
            Locale.of("sv", "SE"),
            Locale.of("ja", "JP"),
            Locale.of("ar", "SA")
    };

    public static final String[] LANGUAGE_NAMES = {
            "English", "Suomi", "Svenska", "日本語", "العربية"
    };

    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        showView(LOCALES[0]);
        stage.show();
    }

    public static void showView(Locale locale) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("MessagesBundle", locale);

        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/app/main-view.fxml"), bundle);
        Parent root = loader.load();

        MainController controller = loader.getController();
        controller.init(locale, bundle);

        if ("ar".equals(locale.getLanguage())) {
            root.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        } else {
            root.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        }

        stage.setTitle(bundle.getString("app.title"));
        stage.setScene(new Scene(root, 760, 540));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
