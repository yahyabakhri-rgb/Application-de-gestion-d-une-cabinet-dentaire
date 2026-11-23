package ma.emi.cabinet.model;

import ma.emi.cabinet.model.enums.StatutRendezVous;

public class RendezVous {

    private long id;
    private Patient patient;
    private Acte acte;
    private CreneauReserve creneau;
    private StatutRendezVous statut;
    private String notes;
    private boolean urgent;

    public RendezVous(long id, Patient patient, Acte acte, CreneauReserve creneau,
                      StatutRendezVous statut, String notes) {
        this(id, patient, acte, creneau, statut, notes, false);
    }

    public RendezVous(long id, Patient patient, Acte acte, CreneauReserve creneau,
                      StatutRendezVous statut, String notes, boolean urgent) {
        this.id = id;
        this.patient = patient;
        this.acte = acte;
        this.creneau = creneau;
        this.statut = statut;
        this.notes = notes;
        this.urgent = urgent;
    }


    public boolean estActif() {
        return statut == StatutRendezVous.EN_ATTENTE || statut == StatutRendezVous.VALIDE;
    }

    public Dentiste getDentiste() {
        return creneau.getDentiste();
    }


    public java.time.LocalDate getDate() {
        return creneau.getDate();
    }

    public java.time.LocalTime getHeure() {
        return creneau.getHeureDebut();
    }

    public String getNomPatient() {
        return patient.getNomComplet();
    }

    public String getNomActe() {
        return acte.getLibelle();
    }

    public String getNomDentiste() {
        return creneau.getDentiste().getNomComplet();
    }

    public long getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Acte getActe() {
        return acte;
    }

    public CreneauReserve getCreneau() {
        return creneau;
    }

    public StatutRendezVous getStatut() {
        return statut;
    }

    public void setStatut(StatutRendezVous statut) {
        this.statut = statut;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public String getType() {
        return urgent ? "Urgence" : "Normal";
    }

    @Override

    public String toString() {
        return creneau.getDate() + " " + creneau.getHeureDebut() + " - " + patient.getNomComplet() + " - "
                + acte.getLibelle() + " - " + creneau.getDentiste() + " (" + statut + ")";
    }
}
