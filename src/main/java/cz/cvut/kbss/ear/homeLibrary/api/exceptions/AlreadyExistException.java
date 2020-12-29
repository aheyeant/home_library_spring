package cz.cvut.kbss.ear.homeLibrary.api.exceptions;

public class AlreadyExistException extends EarException {
    public AlreadyExistException(String message) {
        super(message);
    }

    public static AlreadyExistException create(String resourceName, Object identifier) {
        return new AlreadyExistException(resourceName + " identified by " + identifier + " already exists.");
    }
}
