package ma.emi.cabinet.model;

import java.time.LocalDate;

public class Patient extends Utilisateur {

    private String cin;
    private String telephone;
    private LocalDate dateNaissance;

    public Patient(long id, String nom, String prenom, String email, String motDePasse,
                   String cin, String telephone, LocalDate dateNaissance) {
        super(id, nom, prenom, email, motDePasse);
        this.cin = cin;
        this.telephone = telephone;
        this.dateNaissance = dateNaissance;
    }

    @Override

    public String getRole() {
        return "Patient";
    }

    @Override

    public String toString() {
        return getNomComplet() + " (" + cin + ")";
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
}
