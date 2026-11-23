package ma.emi.cabinet.model;

public class Dentiste {

    private long id;
    private String nom;
    private String prenom;
    private String specialite;
    private String telephone;

    public Dentiste(long id, String nom, String prenom, String specialite, String telephone) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
        this.telephone = telephone;
    }

    public String getNomComplet() {
        return "Dr " + prenom + " " + nom;
    }

    public long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override

    public String toString() {
        return getNomComplet();
    }
}
