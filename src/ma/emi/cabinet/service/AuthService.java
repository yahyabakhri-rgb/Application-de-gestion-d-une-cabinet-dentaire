package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.AuthentificationException;
import ma.emi.cabinet.exception.ValidationException;
import ma.emi.cabinet.model.Administrateur;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.model.Utilisateur;
import ma.emi.cabinet.persistence.AdministrateurRepository;
import ma.emi.cabinet.persistence.PatientRepository;
import ma.emi.cabinet.util.Validation;

public class AuthService {


    public static final String ADMIN_EMAIL = "admin@cabinet.ma";
    public static final String ADMIN_MOT_DE_PASSE = "admin123";

    private AdministrateurRepository administrateurRepository = new AdministrateurRepository();
    private PatientRepository patientRepository = new PatientRepository();

    public Utilisateur connecter(String email, String motDePasse) throws AuthentificationException {
        for (Administrateur a : administrateurRepository.lister()) {
            if (a.getEmail().equalsIgnoreCase(email.trim()) && a.getMotDePasse().equals(motDePasse)) {
                return a;
            }
        }
        for (Patient p : patientRepository.lister()) {
            if (p.getEmail().equalsIgnoreCase(email.trim()) && p.getMotDePasse().equals(motDePasse)) {
                return p;
            }
        }
        throw new AuthentificationException();
    }

    public void creerAdminParDefaut() {
        if (administrateurRepository.lister().isEmpty()) {
            Administrateur admin = new Administrateur(administrateurRepository.nouvelId(), "Admin", "Cabinet",
                    ADMIN_EMAIL, ADMIN_MOT_DE_PASSE);
            administrateurRepository.ajouter(admin);
        }
    }



    public String verifierEmail(String email, Utilisateur moi) throws ValidationException {
        email = Validation.verifierRempli(email, "Email").toLowerCase();
        if (!email.contains("@")) {
            throw new ValidationException("Adresse email invalide.");
        }
        for (Administrateur a : administrateurRepository.lister()) {
            if (a != moi && a.getEmail().equalsIgnoreCase(email)) {
                throw new ValidationException("Cet email est déjà utilisé.");
            }
        }
        for (Patient p : patientRepository.lister()) {
            if (p != moi && p.getEmail().equalsIgnoreCase(email)) {
                throw new ValidationException("Cet email est déjà utilisé.");
            }
        }
        return email;
    }
}
