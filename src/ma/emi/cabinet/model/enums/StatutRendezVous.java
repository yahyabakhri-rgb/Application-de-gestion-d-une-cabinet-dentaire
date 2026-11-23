package ma.emi.cabinet.model.enums;





public enum StatutRendezVous {
    EN_ATTENTE("En attente"),
    VALIDE("Validé"),
    TERMINE("Terminé"),
    ANNULE("Annulé");

    private final String libelle;
    StatutRendezVous(String libelle) {
        this.libelle = libelle;
    }

    @Override

    public String toString() {
        return libelle;
    }
}
