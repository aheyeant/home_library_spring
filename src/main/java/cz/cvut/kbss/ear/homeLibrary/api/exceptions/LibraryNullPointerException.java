package cz.cvut.kbss.ear.homeLibrary.api.exceptions;

public class LibraryNullPointerException extends EarException {

    public LibraryNullPointerException(String message) {
        super(message);
    }

    public static LibraryNullPointerException create(Integer user_id) {
        return new LibraryNullPointerException("LibraryNullPointerException: user_id=" + user_id);
    }
}
