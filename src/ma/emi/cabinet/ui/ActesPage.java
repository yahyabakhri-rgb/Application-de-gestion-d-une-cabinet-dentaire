package ma.emi.cabinet.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.CabinetException;
import ma.emi.cabinet.model.Acte;
import ma.emi.cabinet.service.ActeService;

public class ActesPage {

    ActeService service = new ActeService();
    TableView<Acte> table = new TableView<>();
    TextField code = new TextField(), libelle = new TextField();
    TextField prix = new TextField(), duree = new TextField();

    public void afficher(Stage stage) {
        GridPane form = new GridPane();
        form.setHgap(40);
        Theme.champ(form, "CODE", code, 0, 0);
        Theme.champ(form, "PRIX (DH)", prix, 1, 0);
        Theme.champ(form, "LIBELLE", libelle, 0, 2);
        Theme.champ(form, "DUREE (MIN)", duree, 1, 2);

        Button ajouter = Theme.bouton("AJOUTER", Color.GREEN);
        Button modifier = Theme.bouton("MODIFIER", Color.TEAL);
        Button supprimer = Theme.bouton("SUPPRIMER", Color.RED);
        Button actualiser = Theme.bouton("ACTUALISER", Color.PURPLE);
        Button menu = Theme.bouton("MENU PRINCIPAL", Color.RED);

        Theme.colonne(table, "CODE", "code");
        Theme.colonne(table, "LIBELLE", "libelle");
        Theme.colonne(table, "PRIX", "tarif");
        Theme.colonne(table, "DUREE", "dureeMinutes");

        table.setOnMouseClicked(e -> {
            Acte a = table.getSelectionModel().getSelectedItem();
            if (a != null) {
                code.setText(a.getCode());
                libelle.setText(a.getLibelle());
                prix.setText("" + a.getTarif());
                duree.setText("" + a.getDureeMinutes());
            }
        });

        ajouter.setOnAction(e -> {
            try {
                service.ajouter(code.getText(), libelle.getText(), Double.parseDouble(prix.getText()), Integer.parseInt(duree.getText()));
                actualiser();
            } catch (NumberFormatException ex) {
                Theme.alerte("Le prix et la duree doivent etre des nombres");
            } catch (CabinetException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        modifier.setOnAction(e -> {
            try {
                service.modifier(table.getSelectionModel().getSelectedItem(), code.getText(), libelle.getText(),
                        Double.parseDouble(prix.getText()), Integer.parseInt(duree.getText()));
                actualiser();
            } catch (NumberFormatException ex) {
                Theme.alerte("Le prix et la duree doivent etre des nombres");
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

        VBox gauche = new VBox(15, Theme.titre("AJOUTER LES ACTES"), form);
        stage.setScene(Theme.page(gauche, Theme.panneau(ajouter, modifier, supprimer, actualiser, new Label(""), menu), table));
        actualiser();
    }
    void actualiser() {
        table.getItems().setAll(service.lister());
        code.clear();
        libelle.clear();
        prix.clear();
        duree.clear();
    }
}
