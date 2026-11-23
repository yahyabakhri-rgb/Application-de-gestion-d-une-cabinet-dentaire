package ma.emi.cabinet.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.AuthentificationException;
import ma.emi.cabinet.model.*;
import ma.emi.cabinet.service.AuthService;

public class LoginPage {

    public void afficher(Stage stage) {
        Label titre = new Label("CABINET DENTAIRE");
        titre.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        titre.setTextFill(Color.GREEN);

        Label lblEmail = new Label("EMAIL");
        lblEmail.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        TextField email = new TextField();
        Label lblMdp = new Label("MOT DE PASSE");
        lblMdp.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        PasswordField mdp = new PasswordField();

        Button fermer = Theme.bouton("FERMER", Color.RED);
        Button connecter = Theme.bouton("SE CONNECTER", Color.GREEN);
        fermer.setPrefWidth(140);
        connecter.setPrefWidth(140);

        fermer.setOnAction(e -> stage.close());
        connecter.setOnAction(e -> {
            try {
                Utilisateur u = new AuthService().connecter(email.getText(), mdp.getText());
                if (u instanceof Administrateur) {
                    new MenuPage().afficher(stage);
                } else {
                    new PatientPage().afficher(stage, (Patient) u);
                }
            } catch (AuthentificationException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        HBox boutons = new HBox(10, fermer, connecter);
        boutons.setAlignment(Pos.CENTER);
        VBox root = new VBox(10, titre, lblEmail, email, lblMdp, mdp, boutons);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(root, 340, 330));
    }
}
