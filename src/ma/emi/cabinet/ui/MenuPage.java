package ma.emi.cabinet.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class MenuPage {

    public void afficher(Stage stage) {
        Button patients = Theme.bouton("PATIENTS", Color.TEAL);
        Button dentistes = Theme.bouton("DENTISTES", Color.TEAL);
        Button actes = Theme.bouton("ACTES", Color.TEAL);
        Button creneaux = Theme.bouton("EMPLOI DU TEMPS", Color.TEAL);
        Button rendezVous = Theme.bouton("RENDEZ-VOUS", Color.TEAL);
        Button deconnecter = Theme.bouton("SE DECONNECTER", Color.RED);

        patients.setOnAction(e -> new PatientsPage().afficher(stage));
        dentistes.setOnAction(e -> new DentistesPage().afficher(stage));
        actes.setOnAction(e -> new ActesPage().afficher(stage));
        creneaux.setOnAction(e -> new CreneauxPage().afficher(stage));
        rendezVous.setOnAction(e -> new RendezVousPage().afficher(stage));
        deconnecter.setOnAction(e -> new LoginPage().afficher(stage));


        Label date = new Label("Date : " + LocalDate.now());
        Label message = new Label("EN SERVICE DEPUIS PLUSIEURS ANNEES, NOTRE CABINET EST A VOTRE ECOUTE.\n"
                + "N'HESITEZ PAS A NOUS DEMANDER NOS SERVICES. MERCI !");
        message.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        message.setTextFill(Color.DARKBLUE);
        message.setPadding(new Insets(15));
        message.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        VBox gauche = Theme.panneau(patients, dentistes, actes, creneaux, rendezVous, new Label(""), deconnecter);
        HBox centre = new HBox(30, gauche, new VBox(20, date, message));
        VBox root = new VBox(20, Theme.titre("CABINET DENTAIRE"), centre);
        root.setPadding(new Insets(20));
        stage.setScene(new Scene(root, 900, 480));
    }
}
