package cz.cvut.kbss.ear.homeLibrary.api.exceptions;

public class NotFoundException extends EarException {
    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException create(String resourceName, Object identifier) {
        return new NotFoundException(resourceName + " identified by " + identifier + " not found.");
    }
}
