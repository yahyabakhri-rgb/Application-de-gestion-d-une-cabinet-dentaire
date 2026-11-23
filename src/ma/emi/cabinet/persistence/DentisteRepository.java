package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Dentiste;

import java.util.List;

public class DentisteRepository extends AbstractRepository<Dentiste, Long> {

    protected List<Dentiste> liste() {
        return store.getDentistes();
    }

    protected Long getId(Dentiste dentiste) {
        return dentiste.getId();
    }
}
