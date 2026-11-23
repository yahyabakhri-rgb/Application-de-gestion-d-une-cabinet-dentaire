package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.ValidationException;
import ma.emi.cabinet.model.Dentiste;
import ma.emi.cabinet.persistence.CreneauRepository;
import ma.emi.cabinet.persistence.DentisteRepository;
import ma.emi.cabinet.util.Validation;

import java.util.List;

public class DentisteService {

    private DentisteRepository dentisteRepository = new DentisteRepository();
    private CreneauRepository creneauRepository = new CreneauRepository();

    public Dentiste ajouter(String nom, String prenom, String specialite, String telephone) throws ValidationException {
        nom = Validation.verifierRempli(nom, "Nom");
        prenom = Validation.verifierRempli(prenom, "Prénom");
        Dentiste d = new Dentiste(dentisteRepository.nouvelId(), nom, prenom, specialite.trim(), telephone.trim());
        dentisteRepository.ajouter(d);
        return d;
    }

    public void modifier(Dentiste d, String nom, String prenom, String specialite, String telephone)
            throws ValidationException {
        if (d == null) {
            throw new ValidationException("Choisissez une ligne du tableau.");
        }
        nom = Validation.verifierRempli(nom, "Nom");
        prenom = Validation.verifierRempli(prenom, "Prénom");
        d.setNom(nom);
        d.setPrenom(prenom);
        d.setSpecialite(specialite.trim());
        d.setTelephone(telephone.trim());
        dentisteRepository.sauvegarder();
    }


    public void supprimer(Dentiste d) throws ValidationException {
        if (d == null) {
            throw new ValidationException("Choisissez une ligne du tableau.");
        }
        if (!creneauRepository.trouverParDentiste(d).isEmpty()) {
            throw new ValidationException("Impossible de supprimer ce dentiste : il a des créneaux.");
        }
        dentisteRepository.supprimer(d);
    }

    public List<Dentiste> lister() {
        List<Dentiste> liste = dentisteRepository.lister();
        liste.sort((a, b) -> a.getNom().compareToIgnoreCase(b.getNom()));
        return liste;
    }
}
