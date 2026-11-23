package ma.emi.cabinet.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.CabinetException;
import ma.emi.cabinet.model.Dentiste;
import ma.emi.cabinet.service.DentisteService;

public class DentistesPage {

    DentisteService service = new DentisteService();
    TableView<Dentiste> table = new TableView<>();
    TextField nom = new TextField(), prenom = new TextField();
    TextField specialite = new TextField(), tel = new TextField();

    public void afficher(Stage stage) {
        GridPane form = new GridPane();
        form.setHgap(40);
        Theme.champ(form, "NOM", nom, 0, 0);
        Theme.champ(form, "PRENOM", prenom, 1, 0);
        Theme.champ(form, "SPECIALITE", specialite, 0, 2);
        Theme.champ(form, "TELEPHONE", tel, 1, 2);

        Button ajouter = Theme.bouton("AJOUTER", Color.GREEN);
        Button modifier = Theme.bouton("MODIFIER", Color.TEAL);
        Button supprimer = Theme.bouton("SUPPRIMER", Color.RED);
        Button actualiser = Theme.bouton("ACTUALISER", Color.PURPLE);
        Button menu = Theme.bouton("MENU PRINCIPAL", Color.RED);

        Theme.colonne(table, "CODE", "id");
        Theme.colonne(table, "NOM", "nom");
        Theme.colonne(table, "PRENOM", "prenom");
        Theme.colonne(table, "SPECIALITE", "specialite");
        Theme.colonne(table, "TELEPHONE", "telephone");

        table.setOnMouseClicked(e -> {
            Dentiste d = table.getSelectionModel().getSelectedItem();
            if (d != null) {
                nom.setText(d.getNom());
                prenom.setText(d.getPrenom());
                specialite.setText(d.getSpecialite());
                tel.setText(d.getTelephone());
            }
        });

        ajouter.setOnAction(e -> {
            try {
                service.ajouter(nom.getText(), prenom.getText(), specialite.getText(), tel.getText());
                actualiser();
            } catch (CabinetException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        modifier.setOnAction(e -> {
            try {
                service.modifier(table.getSelectionModel().getSelectedItem(), nom.getText(), prenom.getText(), specialite.getText(), tel.getText());
                actualiser();
            } catch (CabinetException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        supprimer.setOnAction(e -> {
            try {
                service.supprimer(table.getSelectionModel().getSelectedItem());
                actualiser();
            } catch (CabinetException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        actualiser.setOnAction(e -> actualiser());
        menu.setOnAction(e -> new MenuPage().afficher(stage));

        VBox gauche = new VBox(15, Theme.titre("AJOUTER LES DENTISTES"), form);
        stage.setScene(Theme.page(gauche, Theme.panneau(ajouter, modifier, supprimer, actualiser, new Label(""), menu), table));
        actualiser();
    }
    void actualiser() {
        table.getItems().setAll(service.lister());
        nom.clear();
        prenom.clear();
        specialite.clear();
        tel.clear();
    }
}
