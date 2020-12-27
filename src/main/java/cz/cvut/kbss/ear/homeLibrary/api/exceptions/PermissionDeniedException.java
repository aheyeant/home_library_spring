package cz.cvut.kbss.ear.homeLibrary.api.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class PermissionDeniedException extends RuntimeException {
}
