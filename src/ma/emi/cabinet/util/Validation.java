package ma.emi.cabinet.util;

import ma.emi.cabinet.exception.ValidationException;

public class Validation {


    public static String verifierRempli(String valeur, String champ) throws ValidationException {
        if (valeur == null || valeur.trim().isEmpty()) {
            throw new ValidationException("Le champ " + champ + " est obligatoire.");
        }
        return valeur.trim();
    }
}
