package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.ValidationException;
import ma.emi.cabinet.model.Creneau;
import ma.emi.cabinet.model.Dentiste;
import ma.emi.cabinet.persistence.CreneauRepository;
import ma.emi.cabinet.persistence.RendezVousRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CreneauService {

    private CreneauRepository creneauRepository = new CreneauRepository();
    private RendezVousRepository rendezVousRepository = new RendezVousRepository();

    public Creneau ajouter(Dentiste dentiste, LocalDate date, LocalTime debut, LocalTime fin)
            throws ValidationException {
        if (dentiste == null || date == null || debut == null || fin == null) {
            throw new ValidationException("Le dentiste, la date et les heures sont obligatoires.");
        }
        if (!fin.isAfter(debut)) {
            throw new ValidationException("L'heure de fin doit être après l'heure de début.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new ValidationException("On ne peut pas créer un créneau dans le passé.");
        }

        Creneau nouveau = new Creneau(creneauRepository.nouvelId(), dentiste, date, debut, fin);
        for (Creneau c : creneauRepository.trouverParDentiste(dentiste)) {
            if (c.chevauche(nouveau)) {
                throw new ValidationException("Ce créneau chevauche un créneau existant : " + c);
            }
        }
        creneauRepository.ajouter(nouveau);
        return nouveau;
    }


    public void supprimer(Creneau creneau) throws ValidationException {
        if (rendezVousRepository.existePourCreneau(creneau)) {
            throw new ValidationException("Impossible de supprimer ce créneau : il est lié à un rendez-vous.");
        }
        creneauRepository.supprimer(creneau);
    }

    public List<Creneau> lister() {
        List<Creneau> liste = creneauRepository.lister();
        trier(liste);
        return liste;
    }

    public List<Creneau> listerParDentiste(Dentiste dentiste) {
        List<Creneau> liste = creneauRepository.trouverParDentiste(dentiste);
        trier(liste);
        return liste;
    }


    public List<Creneau> listerLibres(Dentiste dentiste) {
        List<Creneau> libres = new ArrayList<>();
        for (Creneau c : listerParDentiste(dentiste)) {
            if (!c.estReserve() && c.getDate().atTime(c.getHeureDebut()).isAfter(LocalDateTime.now())) {
                libres.add(c);
            }
        }
        return libres;
    }


    private void trier(List<Creneau> liste) {
        liste.sort((a, b) -> {
            if (!a.getDate().equals(b.getDate())) {
                return a.getDate().compareTo(b.getDate());
            }
            return a.getHeureDebut().compareTo(b.getHeureDebut());
        });
    }
}
