package cz.cvut.kbss.ear.homeLibrary.api.exceptions;

public class PersistenceException extends HLException {
    public PersistenceException(String message) {
        super(message);
    }

    public static PersistenceException create(String resourceName, Object identifier) {
        return new PersistenceException(resourceName + " identified by " + identifier + " not found.");
    }
}
