package ma.emi.cabinet.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.ReservationException;
import ma.emi.cabinet.model.RendezVous;
import ma.emi.cabinet.service.ReservationService;

public class RendezVousPage {

    ReservationService service = new ReservationService();
    TableView<RendezVous> table = new TableView<>();

    public void afficher(Stage stage) {
        Label info = new Label("CLIQUEZ SUR UN RENDEZ-VOUS DU TABLEAU\nPUIS CHOISISSEZ UNE ACTION A DROITE.");
        info.setTextFill(Color.PURPLE);

        Button valider = Theme.bouton("VALIDER", Color.GREEN);
        Button terminer = Theme.bouton("TERMINER", Color.TEAL);
        Button annuler = Theme.bouton("ANNULER", Color.RED);
        Button fiche = Theme.bouton("FICHE (PDF)", Color.PURPLE);
        Button menu = Theme.bouton("MENU PRINCIPAL", Color.RED);

        Theme.colonne(table, "CODE", "id");
        Theme.colonne(table, "DATE", "date");
        Theme.colonne(table, "HEURE", "heure");
        Theme.colonne(table, "PATIENT", "nomPatient");
        Theme.colonne(table, "ACTE", "nomActe");
        Theme.colonne(table, "DENTISTE", "nomDentiste");
        Theme.colonne(table, "STATUT", "statut");
        Theme.colonne(table, "TYPE", "type");

        valider.setOnAction(e -> {
            try {
                service.valider(table.getSelectionModel().getSelectedItem());
                actualiser();
            } catch (ReservationException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        terminer.setOnAction(e -> {
            try {
                service.terminer(table.getSelectionModel().getSelectedItem());
                actualiser();
            } catch (ReservationException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        annuler.setOnAction(e -> {
            try {
                service.annuler(table.getSelectionModel().getSelectedItem());
                actualiser();
            } catch (ReservationException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        fiche.setOnAction(e -> Theme.fiche(stage, table.getSelectionModel().getSelectedItem()));
        menu.setOnAction(e -> new MenuPage().afficher(stage));

        VBox gauche = new VBox(15, Theme.titre("GERER LES RENDEZ-VOUS"), info);
        stage.setScene(Theme.page(gauche, Theme.panneau(valider, terminer, annuler, fiche, new Label(""), menu), table));
        actualiser();
    }
    void actualiser() {
        table.getItems().setAll(service.lister());
    }
}
