package ma.emi.cabinet.persistence;

import java.util.List;



public interface Repository<T, ID> {
    List<T> lister();
    T trouverParId(ID id);
    void ajouter(T objet);
    void supprimer(T objet);


    void sauvegarder();
}
