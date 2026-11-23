package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Creneau;
import ma.emi.cabinet.model.Dentiste;

import java.util.ArrayList;
import java.util.List;

public class CreneauRepository extends AbstractRepository<Creneau, Long> {

    protected List<Creneau> liste() {
        return store.getCreneaux();
    }

    protected Long getId(Creneau creneau) {
        return creneau.getId();
    }

    public List<Creneau> trouverParDentiste(Dentiste dentiste) {
        List<Creneau> resultat = new ArrayList<>();
        for (Creneau c : liste()) {
            if (c.getDentiste().getId() == dentiste.getId()) {
                resultat.add(c);
            }
        }
        return resultat;
    }



    public void remplacer(Creneau nouveau) {
        for (int i = 0; i < liste().size(); i++) {
            if (liste().get(i).getId() == nouveau.getId()) {
                liste().set(i, nouveau);
            }
        }
        store.sauvegarder();
    }
}
