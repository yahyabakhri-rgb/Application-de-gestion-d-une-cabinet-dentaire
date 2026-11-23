package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Acte;
import ma.emi.cabinet.model.Creneau;
import ma.emi.cabinet.model.Patient;
import ma.emi.cabinet.model.RendezVous;

import java.util.ArrayList;
import java.util.List;

public class RendezVousRepository extends AbstractRepository<RendezVous, Long> {

    protected List<RendezVous> liste() {
        return store.getRendezVous();
    }

    protected Long getId(RendezVous rdv) {
        return rdv.getId();
    }

    public List<RendezVous> trouverParPatient(Patient patient) {
        List<RendezVous> resultat = new ArrayList<>();
        for (RendezVous r : liste()) {
            if (r.getPatient().getId() == patient.getId()) {
                resultat.add(r);
            }
        }
        return resultat;
    }

    public boolean existePourActe(Acte acte) {
        for (RendezVous r : liste()) {
            if (r.getActe().getId() == acte.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean existePourCreneau(Creneau creneau) {
        for (RendezVous r : liste()) {
            if (r.getCreneau().getId() == creneau.getId()) {
                return true;
            }
        }
        return false;
    }
}
