package ma.emi.cabinet.persistence;

import java.util.ArrayList;
import java.util.List;



public abstract class AbstractRepository<T, ID> implements Repository<T, ID> {

    protected DataStore store = DataStore.getInstance();


    protected abstract List<T> liste();


    protected abstract ID getId(T objet);

    public List<T> lister() {
        return new ArrayList<>(liste());
    }

    public T trouverParId(ID id) {
        for (T objet : liste()) {
            if (getId(objet).equals(id)) {
                return objet;
            }
        }
        return null;
    }

    public void ajouter(T objet) {
        liste().add(objet);
        store.sauvegarder();
    }

    public void supprimer(T objet) {
        liste().remove(objet);
        store.sauvegarder();
    }

    public void sauvegarder() {
        store.sauvegarder();
    }

    public long nouvelId() {
        return store.nouvelId();
    }
}
