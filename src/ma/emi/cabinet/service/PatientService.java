package ma.emi.cabinet.service;

import ma.emi.cabinet.exception.ValidationException;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.persistence.PatientRepository;
import ma.emi.cabinet.persistence.RendezVousRepository;
import ma.emi.cabinet.util.Validation;

import java.time.LocalDate;
import java.util.List;

public class PatientService {

    private PatientRepository patientRepository = new PatientRepository();
    private RendezVousRepository rendezVousRepository = new RendezVousRepository();
    private AuthService authService = new AuthService();

    public Patient ajouter(String nom, String prenom, String email, String motDePasse,
                           String cin, String telephone, LocalDate dateNaissance) throws ValidationException {
        nom = Validation.verifierRempli(nom, "Nom");
        prenom = Validation.verifierRempli(prenom, "Prénom");
        email = authService.verifierEmail(email, null);
        cin = verifierCin(cin, null);
        if (motDePasse == null || motDePasse.length() < 4) {
            throw new ValidationException("Le mot de passe doit avoir au moins 4 caractères.");
        }
        verifierDateNaissance(dateNaissance);

        Patient p = new Patient(patientRepository.nouvelId(), nom, prenom, email, motDePasse, cin,
                telephone.trim(), dateNaissance);
        patientRepository.ajouter(p);
        return p;
    }

    public void modifier(Patient p, String nom, String prenom, String email,
                         String cin, String telephone, LocalDate dateNaissance) throws ValidationException {
        if (p == null) {
            throw new ValidationException("Choisissez une ligne du tableau.");
        }

        nom = Validation.verifierRempli(nom, "Nom");
        prenom = Validation.verifierRempli(prenom, "Prénom");
        email = authService.verifierEmail(email, p);
        cin = verifierCin(cin, p);
        verifierDateNaissance(dateNaissance);

        p.setNom(nom);
        p.setPrenom(prenom);
        p.setEmail(email);
        p.setCin(cin);
        p.setTelephone(telephone.trim());
        p.setDateNaissance(dateNaissance);
        patientRepository.sauvegarder();
    }

    public void supprimer(Patient p) throws ValidationException {
        if (p == null) {
            throw new ValidationException("Choisissez une ligne du tableau.");
        }
        if (!rendezVousRepository.trouverParPatient(p).isEmpty()) {
            throw new ValidationException("Impossible de supprimer ce patient : il a des rendez-vous.");
        }
        patientRepository.supprimer(p);
    }

    public List<Patient> lister() {
        List<Patient> liste = patientRepository.lister();
        liste.sort((a, b) -> a.getNom().compareToIgnoreCase(b.getNom()));
        return liste;
    }

    private String verifierCin(String cin, Patient moi) throws ValidationException {
        cin = Validation.verifierRempli(cin, "CIN").toUpperCase();
        Patient existant = patientRepository.trouverParCin(cin);
        if (existant != null && existant != moi) {
            throw new ValidationException("Un patient avec ce CIN existe déjà.");
        }
        return cin;
    }

    private void verifierDateNaissance(LocalDate date) throws ValidationException {
        if (date != null && date.isAfter(LocalDate.now())) {
            throw new ValidationException("La date de naissance ne peut pas être dans le futur.");
        }
    }
}
