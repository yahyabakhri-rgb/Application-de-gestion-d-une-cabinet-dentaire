package ma.emi.cabinet.persistence;

import ma.emi.cabinet.model.Patient;

import java.util.List;

public class PatientRepository extends AbstractRepository<Patient, Long> {

    protected List<Patient> liste() {
        return store.getPatients();
    }

    protected Long getId(Patient patient) {
        return patient.getId();
    }


    public Patient trouverParCin(String cin) {
        for (Patient p : liste()) {
            if (p.getCin().equalsIgnoreCase(cin)) {
                return p;
            }
        }
        return null;
    }
}
