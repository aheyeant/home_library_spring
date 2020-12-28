package cz.cvut.kbss.ear.homeLibrary.api.exceptions;

/**
 * Base for all application-specific exceptions.
 */
public class HLException extends RuntimeException {

    public HLException() {
    }

    public HLException(String message) {
        super(message);
    }

    public HLException(String message, Throwable cause) {
        super(message, cause);
    }

    public HLException(Throwable cause) {
        super(cause);
    }
}