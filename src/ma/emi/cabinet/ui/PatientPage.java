package ma.emi.cabinet.ui;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.ReservationException;
import ma.emi.cabinet.model.*;
import ma.emi.cabinet.service.*;



public class PatientPage {

    ReservationService service = new ReservationService();
    TableView<RendezVous> table = new TableView<>();
    Patient patient;

    public void afficher(Stage stage, Patient patient) {
        this.patient = patient;

        ComboBox<Acte> acte = new ComboBox<>();
        acte.getItems().addAll(new ActeService().lister());
        ComboBox<Dentiste> dentiste = new ComboBox<>();
        dentiste.getItems().addAll(new DentisteService().lister());
        ComboBox<Creneau> creneau = new ComboBox<>();
        ComboBox<String> typeRdv = new ComboBox<>();
        typeRdv.getItems().addAll("Normal", "Urgence");
        typeRdv.setValue("Normal");
        typeRdv.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white;");
        typeRdv.setOnAction(e -> typeRdv.setStyle(typeRdv.getValue().equals("Urgence")
                ? "-fx-background-color: #EF6C00; -fx-text-fill: white;"
                : "-fx-background-color: #2E7D32; -fx-text-fill: white;"));
        TextField notes = new TextField();

        creneau.setCellFactory(liste -> new ListCell<Creneau>() {
            @Override
            protected void updateItem(Creneau item, boolean vide) {
                super.updateItem(item, vide);
                setText(vide || item == null ? null : item.toString());
                setStyle(vide ? "" : "-fx-background-color: #2E7D32; -fx-text-fill: white;");
            }
        });
        creneau.setButtonCell(new ListCell<Creneau>() {
            @Override
            protected void updateItem(Creneau item, boolean vide) {
                super.updateItem(item, vide);
                setText(vide || item == null ? "Choisir un créneau" : item.toString());
                setStyle(vide || item == null ? "" : "-fx-background-color: #1565C0; -fx-text-fill: white;");
            }
        });

        GridPane form = new GridPane();
        form.setHgap(40);
        Theme.champ(form, "SOIN", acte, 0, 0);
        Theme.champ(form, "DENTISTE", dentiste, 1, 0);
        Theme.champ(form, "CRENEAU LIBRE", creneau, 0, 2);
        Theme.champ(form, "MESSAGE", notes, 1, 2);
        Theme.champ(form, "TYPE DE RENDEZ-VOUS", typeRdv, 0, 4);


        dentiste.setOnAction(e -> creneau.getItems().setAll(new CreneauService().listerLibres(dentiste.getValue())));

        Button reserver = Theme.bouton("RESERVER", Color.GREEN);
        Button annuler = Theme.bouton("ANNULER LE RDV", Color.RED);
        Button fiche = Theme.bouton("FICHE (PDF)", Color.PURPLE);
        Button deconnecter = Theme.bouton("SE DECONNECTER", Color.RED);

        Theme.colonne(table, "CODE", "id");
        Theme.colonne(table, "DATE", "date");
        Theme.colonne(table, "HEURE", "heure");
        Theme.colonne(table, "SOIN", "nomActe");
        Theme.colonne(table, "DENTISTE", "nomDentiste");
        Theme.colonne(table, "STATUT", "statut");
        Theme.colonne(table, "TYPE", "type");

        reserver.setOnAction(e -> {
            try {
                service.reserver(patient, acte.getValue(), creneau.getValue(), notes.getText(),
                        "Urgence".equals(typeRdv.getValue()));
                Theme.alerte("Demande envoyée, le cabinet doit la valider");
                creneau.getItems().remove(creneau.getValue());
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
        deconnecter.setOnAction(e -> new LoginPage().afficher(stage));

        VBox gauche = new VBox(15, Theme.titre("BONJOUR " + patient.getNomComplet().toUpperCase()), form);
        stage.setScene(Theme.page(gauche, Theme.panneau(reserver, annuler, fiche, new Label(""), deconnecter), table));
        actualiser();
    }
    void actualiser() {
        table.getItems().setAll(service.listerParPatient(patient));
    }
}
