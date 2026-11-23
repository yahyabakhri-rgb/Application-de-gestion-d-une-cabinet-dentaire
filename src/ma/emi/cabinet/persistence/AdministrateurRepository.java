package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Administrateur;

import java.util.List;

public class AdministrateurRepository extends AbstractRepository<Administrateur, Long> {

    protected List<Administrateur> liste() {
        return store.getAdministrateurs();
    }

    protected Long getId(Administrateur admin) {
        return admin.getId();
    }
}
