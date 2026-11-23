package ma.emi.cabinet.ui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ma.emi.cabinet.exception.ValidationException;
import ma.emi.cabinet.model.*;
import ma.emi.cabinet.service.*;

import java.time.*;



public class CreneauxPage {

    CreneauService service = new CreneauService();
    ComboBox<Dentiste> dentiste = new ComboBox<>();
    GridPane grille = new GridPane();
    LocalDate lundi = LocalDate.now().with(DayOfWeek.MONDAY);

    public void afficher(Stage stage) {
        dentiste.getItems().addAll(new DentisteService().lister());
        Button precedente = new Button("< SEMAINE");
        Button suivante = new Button("SEMAINE >");
        Button menu = Theme.bouton("MENU PRINCIPAL", Color.RED);

        dentiste.setOnAction(e -> remplir());
        precedente.setOnAction(e -> {
            lundi = lundi.minusDays(7);
            remplir();
        });
        suivante.setOnAction(e -> {
            lundi = lundi.plusDays(7);
            remplir();
        });
        menu.setOnAction(e -> new MenuPage().afficher(stage));

        HBox haut = new HBox(10, new Label("DENTISTE"), dentiste, precedente, suivante, menu);
        VBox root = new VBox(10, Theme.titre("EMPLOI DU TEMPS"), haut, grille);
        root.setPadding(new Insets(15));
        stage.setScene(new Scene(root, 720, 700));
    }
    void remplir() {
        grille.getChildren().clear();
        for (int j = 0; j < 6; j++) {
            grille.add(new Label(" " + lundi.plusDays(j)), j + 1, 0);
        }


        for (int i = 0; i < 20; i++) {
            LocalTime heure = LocalTime.of(8, 0).plusMinutes(i * 30);
            grille.add(new Label(heure + " "), 0, i + 1);

            for (int j = 0; j < 6; j++) {
                LocalDate jour = lundi.plusDays(j);
                Button b = new Button("+");
                b.setPrefWidth(95);

                Creneau trouve = null;
                for (Creneau c : service.listerParDentiste(dentiste.getValue())) {
                    if (c.getDate().equals(jour) && c.getHeureDebut().equals(heure)) {
                        trouve = c;
                    }
                }

                if (trouve == null) {
                    b.setOnAction(e -> {
                        try {
                            service.ajouter(dentiste.getValue(), jour, heure, heure.plusMinutes(30));
                            remplir();
                        } catch (ValidationException ex) {
                            Theme.alerte(ex.getMessage());
                        }
                    });
                } else if (trouve.estReserve()) {
                    RendezVous rdv = new ReservationService().rendezVousDuCreneau(trouve);
                    if (rdv != null && rdv.isUrgent()) {
                        b.setText("URGENCE");
                        b.setStyle("-fx-background-color: #EF6C00; -fx-text-fill: white;");
                    } else {
                        b.setText("RESERVE");
                        b.setStyle("-fx-background-color: #C62828; -fx-text-fill: white;");
                    }
                } else {
                    Creneau c = trouve;
                    b.setText("LIBRE");
                    b.setStyle("-fx-background-color: #2E7D32; -fx-text-fill: white;");
                    b.setOnAction(e -> {
                        try {
                            service.supprimer(c);
                            remplir();
                        } catch (ValidationException ex) {
                            Theme.alerte(ex.getMessage());
                        }
                    });
                }
                grille.add(b, j + 1, i + 1);
            }
        }
    }
}
