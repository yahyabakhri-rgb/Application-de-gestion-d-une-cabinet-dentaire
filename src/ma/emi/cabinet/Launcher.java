package ma.emi.cabinet;

import javafx.application.Application;
import ma.emi.cabinet.exception.CabinetException;
import ma.emi.cabinet.service.DonneesInitiales;
import ma.emi.cabinet.ui.MainApp;



public class Launcher {

    public static void main(String[] args) {
        try {
            new DonneesInitiales().chargerSiBesoin();
        } catch (CabinetException e) {
            System.out.println("Erreur pendant la création des données de test : " + e.getMessage());
        }
        Application.launch(MainApp.class, args);
    }
}
