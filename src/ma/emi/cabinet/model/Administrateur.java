package ma.emi.cabinet.model;

public class Administrateur extends Utilisateur {
    public Administrateur(long id, String nom, String prenom, String email, String motDePasse) {
        super(id, nom, prenom, email, motDePasse);
    }

    @Override
    public String getRole() {
        return "Administrateur";
    }
}
