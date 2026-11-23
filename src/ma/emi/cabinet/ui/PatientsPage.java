package ma.emi.cabinet.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.CabinetException;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.service.PatientService;

public class PatientsPage {

    PatientService service = new PatientService();
    TableView<Patient> table = new TableView<>();
    TextField nom = new TextField(), prenom = new TextField(), cin = new TextField();
    TextField tel = new TextField(), email = new TextField(), mdp = new TextField();

    public void afficher(Stage stage) {
        GridPane form = new GridPane();
        form.setHgap(40);
        Theme.champ(form, "NOM", nom, 0, 0);
        Theme.champ(form, "PRENOM", prenom, 1, 0);
        Theme.champ(form, "CIN", cin, 0, 2);
        Theme.champ(form, "TELEPHONE", tel, 1, 2);
        Theme.champ(form, "EMAIL", email, 0, 4);
        Theme.champ(form, "MOT DE PASSE", mdp, 1, 4);

        Button ajouter = Theme.bouton("AJOUTER", Color.GREEN);
        Button modifier = Theme.bouton("MODIFIER", Color.TEAL);
        Button supprimer = Theme.bouton("SUPPRIMER", Color.RED);
        Button actualiser = Theme.bouton("ACTUALISER", Color.PURPLE);
        Button menu = Theme.bouton("MENU PRINCIPAL", Color.RED);

        Theme.colonne(table, "CODE", "id");
        Theme.colonne(table, "NOM", "nom");
        Theme.colonne(table, "PRENOM", "prenom");
        Theme.colonne(table, "CIN", "cin");
        Theme.colonne(table, "TELEPHONE", "telephone");
        Theme.colonne(table, "EMAIL", "email");

        table.setOnMouseClicked(e -> {
            Patient p = table.getSelectionModel().getSelectedItem();
            if (p != null) {
                nom.setText(p.getNom());
                prenom.setText(p.getPrenom());
                cin.setText(p.getCin());
                tel.setText(p.getTelephone());
                email.setText(p.getEmail());
                mdp.setText(p.getMotDePasse());
            }
        });

        ajouter.setOnAction(e -> {
            try {
                service.ajouter(nom.getText(), prenom.getText(), email.getText(), mdp.getText(), cin.getText(), tel.getText(), null);
                actualiser();
            } catch (CabinetException ex) {
                Theme.alerte(ex.getMessage());
            }
        });

        modifier.setOnAction(e -> {
            Patient p = table.getSelectionModel().getSelectedItem();
            try {
                service.modifier(p, nom.getText(), prenom.getText(), email.getText(), cin.getText(), tel.getText(),
                        p == null ? null : p.getDateNaissance());
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

        VBox gauche = new VBox(15, Theme.titre("AJOUTER LES PATIENTS"), form);
        stage.setScene(Theme.page(gauche, Theme.panneau(ajouter, modifier, supprimer, actualiser, new Label(""), menu), table));
        actualiser();
    }
    void actualiser() {
        table.getItems().setAll(service.lister());
        nom.clear();
        prenom.clear();
        cin.clear();
        tel.clear();
        email.clear();
        mdp.clear();
    }
}
