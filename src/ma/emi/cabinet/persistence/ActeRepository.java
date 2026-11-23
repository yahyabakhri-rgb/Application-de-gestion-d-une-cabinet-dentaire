package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Acte;

import java.util.List;

public class ActeRepository extends AbstractRepository<Acte, Long> {

    protected List<Acte> liste() {
        return store.getActes();
    }

    protected Long getId(Acte acte) {
        return acte.getId();
    }


    public Acte trouverParCode(String code) {
        for (Acte a : liste()) {
            if (a.getCode().equalsIgnoreCase(code)) {
                return a;
            }
        }
        return null;
    }
}
