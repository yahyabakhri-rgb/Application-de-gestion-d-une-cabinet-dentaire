package ma.emi.cabinet.model;



public class Acte {

    private long id;
    private String code;
    private String libelle;
    private double tarif;
    private int dureeMinutes;

    public Acte(long id, String code, String libelle, double tarif, int dureeMinutes) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.tarif = tarif;
        this.dureeMinutes = dureeMinutes;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getTarif() {
        return tarif;
    }

    public void setTarif(double tarif) {
        this.tarif = tarif;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(int dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    @Override

    public String toString() {
        return code + " - " + libelle + " (" + tarif + " DH, " + dureeMinutes + " min)";
    }
}
