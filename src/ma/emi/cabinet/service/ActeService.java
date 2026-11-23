package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.ValidationException;
import ma.emi.cabinet.model.Acte;
import ma.emi.cabinet.persistence.ActeRepository;
import ma.emi.cabinet.persistence.RendezVousRepository;
import ma.emi.cabinet.util.Validation;

import java.util.List;

public class ActeService {

    private ActeRepository acteRepository = new ActeRepository();
    private RendezVousRepository rendezVousRepository = new RendezVousRepository();

    public Acte ajouter(String code, String libelle, double tarif, int dureeMinutes) throws ValidationException {
        code = verifierCode(code, null);
        libelle = Validation.verifierRempli(libelle, "Libellé");
        verifierTarifEtDuree(tarif, dureeMinutes);
        Acte a = new Acte(acteRepository.nouvelId(), code, libelle, tarif, dureeMinutes);
        acteRepository.ajouter(a);
        return a;
    }

    public void modifier(Acte a, String code, String libelle, double tarif, int dureeMinutes)
            throws ValidationException {
        if (a == null) {
            throw new ValidationException("Choisissez une ligne du tableau.");
        }
        code = verifierCode(code, a);
        libelle = Validation.verifierRempli(libelle, "Libellé");
        verifierTarifEtDuree(tarif, dureeMinutes);
        a.setCode(code);
        a.setLibelle(libelle);
        a.setTarif(tarif);
        a.setDureeMinutes(dureeMinutes);
        acteRepository.sauvegarder();
    }

    public void supprimer(Acte a) throws ValidationException {
        if (a == null) {
            throw new ValidationException("Choisissez une ligne du tableau.");
        }
        if (rendezVousRepository.existePourActe(a)) {
            throw new ValidationException("Impossible de supprimer cet acte : il est utilisé dans des rendez-vous.");
        }
        acteRepository.supprimer(a);
    }

    public List<Acte> lister() {
        List<Acte> liste = acteRepository.lister();
        liste.sort((a, b) -> a.getCode().compareTo(b.getCode()));
        return liste;
    }

    private String verifierCode(String code, Acte moi) throws ValidationException {
        code = Validation.verifierRempli(code, "Code").toUpperCase();
        Acte existant = acteRepository.trouverParCode(code);
        if (existant != null && existant != moi) {
            throw new ValidationException("Un acte avec ce code existe déjà.");
        }
        return code;
    }

    private void verifierTarifEtDuree(double tarif, int dureeMinutes) throws ValidationException {
        if (tarif < 0) {
            throw new ValidationException("Le tarif ne peut pas être négatif.");
        }
        if (dureeMinutes <= 0) {
            throw new ValidationException("La durée doit être supérieure à 0.");
        }
    }
}
