package ma.emi.cabinet.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ma.emi.cabinet.model.RendezVous;
import ma.emi.cabinet.service.FicheService;

import java.io.File;



public class Theme {

    public static Label titre(String texte) {
        Label l = new Label(texte);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        l.setTextFill(Color.TEAL);
        return l;
    }


    public static void champ(GridPane grille, String texte, Control champ, int colonne, int ligne) {
        Label l = new Label(texte);
        l.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        grille.add(l, colonne, ligne);
        grille.add(champ, colonne, ligne + 1);
    }

    public static Button bouton(String texte, Color couleur) {
        Button b = new Button(texte);
        b.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        b.setTextFill(couleur);
        b.setPrefSize(220, 35);
        return b;
    }


    public static VBox panneau(Node... elements) {
        VBox v = new VBox(10, elements);
        v.setPadding(new Insets(10));
        v.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
        return v;
    }


    public static <T> void colonne(TableView<T> table, String titre, String propriete) {
        TableColumn<T, Object> c = new TableColumn<>(titre);
        c.setCellValueFactory(new PropertyValueFactory<>(propriete));
        table.getColumns().add(c);
    }


    public static Scene page(Node gauche, Node droite, Node bas) {
        VBox root = new VBox(20, new HBox(30, gauche, droite), bas);
        root.setPadding(new Insets(15));
        return new Scene(root, 1000, 600);
    }

    public static void alerte(String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }


    public static void fiche(Stage stage, RendezVous r) {
        FileChooser choix = new FileChooser();
        choix.setInitialFileName("fiche-rdv.pdf");
        File fichier = choix.showSaveDialog(stage);
        if (fichier == null) {
            return;
        }
        try {
            new FicheService().exporterPdf(r, fichier);
            alerte("Fiche enregistrée : " + fichier.getAbsolutePath());
        } catch (Exception ex) {
            alerte(ex.getMessage());
        }
    }
}
