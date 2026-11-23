package ma.emi.cabinet.ui;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override

    public void start(Stage stage) {
        stage.setTitle("Cabinet Dentaire");
        new LoginPage().afficher(stage);
        stage.show();
    }
}
