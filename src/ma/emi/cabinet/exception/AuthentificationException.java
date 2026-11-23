package ma.emi.cabinet.exception;



public class AuthentificationException extends CabinetException {

    public AuthentificationException() {
        super("Email ou mot de passe incorrect.");
    }
}
