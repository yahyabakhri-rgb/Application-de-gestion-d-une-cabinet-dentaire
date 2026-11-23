package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.CabinetException;
import ma.emi.cabinet.model.Acte;
import ma.emi.cabinet.model.Creneau;
import ma.emi.cabinet.model.Dentiste;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.model.RendezVous;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;




public class DonneesInitiales {

    private AuthService authService = new AuthService();
    private PatientService patientService = new PatientService();
    private DentisteService dentisteService = new DentisteService();
    private ActeService acteService = new ActeService();
    private CreneauService creneauService = new CreneauService();
    private ReservationService reservationService = new ReservationService();


    public void chargerSiBesoin() throws CabinetException {
        authService.creerAdminParDefaut();
        if (!patientService.lister().isEmpty() || !dentisteService.lister().isEmpty()) {
            return;
        }

        Patient sara = patientService.ajouter("Alaoui", "Sara", "sara@mail.ma", "patient123",
                "AB123456", "0612345678", LocalDate.of(1992, 4, 12));
        Patient youssef = patientService.ajouter("Bennani", "Youssef", "youssef@mail.ma", "patient123",
                "CD789012", "0698765432", LocalDate.of(1985, 11, 3));

        Dentiste tazi = dentisteService.ajouter("Tazi", "Omar", "Orthodontie", "0522112233");
        Dentiste idrissi = dentisteService.ajouter("Idrissi", "Leila", "Généraliste", "0522445566");
        Dentiste fassi = dentisteService.ajouter("Fassi", "Karim", "Chirurgie dentaire", "0522778899");

        Acte consultation = acteService.ajouter("CONS", "Consultation", 200, 30);
        Acte detartrage = acteService.ajouter("DET", "Détartrage", 300, 30);
        acteService.ajouter("SOIN", "Soin de carie", 400, 30);
        acteService.ajouter("EXT", "Extraction", 500, 60);
        acteService.ajouter("BLAN", "Blanchiment", 1500, 60);



        LocalDate premierJour = null;
        LocalDate jour = LocalDate.now().plusDays(1);
        int nbJours = 0;
        while (nbJours < 10) {
            if (jour.getDayOfWeek() != DayOfWeek.SATURDAY && jour.getDayOfWeek() != DayOfWeek.SUNDAY) {
                if (premierJour == null) {
                    premierJour = jour;
                }
                creerJournee(tazi, jour, 30);
                creerJournee(idrissi, jour, 30);
                creerJournee(fassi, jour, 60);
                nbJours++;
            }
            jour = jour.plusDays(1);
        }


        RendezVous rdvSara = reservationService.reserver(sara, detartrage,
                trouverCreneau(tazi, premierJour, LocalTime.of(9, 0)), "Première visite");
        reservationService.valider(rdvSara);
        reservationService.reserver(youssef, consultation,
                trouverCreneau(idrissi, premierJour, LocalTime.of(10, 0)), "");
    }


    private void creerJournee(Dentiste dentiste, LocalDate jour, int duree) throws CabinetException {
        LocalTime h = LocalTime.of(9, 0);
        while (!h.plusMinutes(duree).isAfter(LocalTime.of(12, 0))) {
            creneauService.ajouter(dentiste, jour, h, h.plusMinutes(duree));
            h = h.plusMinutes(duree);
        }
        h = LocalTime.of(14, 0);
        while (!h.plusMinutes(duree).isAfter(LocalTime.of(16, 0))) {
            creneauService.ajouter(dentiste, jour, h, h.plusMinutes(duree));
            h = h.plusMinutes(duree);
        }
    }

    private Creneau trouverCreneau(Dentiste dentiste, LocalDate jour, LocalTime heure) {
        for (Creneau c : creneauService.listerParDentiste(dentiste)) {
            if (c.getDate().equals(jour) && c.getHeureDebut().equals(heure)) {
                return c;
            }
        }
        return null;
    }
}
