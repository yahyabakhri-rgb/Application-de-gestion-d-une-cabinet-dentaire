package ma.emi.cabinet.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;




public class Creneau {

    private long id;
    private Dentiste dentiste;
    private LocalDate date;
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Creneau(long id, Dentiste dentiste, LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.dentiste = dentiste;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public boolean estReserve() {
        return false;
    }


    public String getEtat() {
        return "Libre";
    }

    public long getDureeMinutes() {
        return Duration.between(heureDebut, heureFin).toMinutes();
    }


    public boolean chevauche(Creneau autre) {
        return date.equals(autre.date)
                && heureDebut.isBefore(autre.heureFin)
                && autre.heureDebut.isBefore(heureFin);
    }

    public long getId() {
        return id;
    }

    public Dentiste getDentiste() {
        return dentiste;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    @Override

    public String toString() {
        return date + " " + heureDebut + "-" + heureFin + " (" + dentiste + ") - " + getEtat();
    }
}
