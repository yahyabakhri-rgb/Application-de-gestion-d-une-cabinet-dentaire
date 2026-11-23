package ma.emi.cabinet.model;

import java.time.LocalDateTime;

public class CreneauReserve extends Creneau {

    private LocalDateTime dateReservation;

    public CreneauReserve(Creneau creneau, LocalDateTime dateReservation) {
        super(creneau.getId(), creneau.getDentiste(), creneau.getDate(),
                creneau.getHeureDebut(), creneau.getHeureFin());
        this.dateReservation = dateReservation;
    }

    @Override

    public boolean estReserve() {
        return true;
    }

    @Override

    public String getEtat() {
        return "Réservé";
    }


    public Creneau liberer() {
        return new Creneau(getId(), getDentiste(), getDate(), getHeureDebut(), getHeureFin());
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }
}
