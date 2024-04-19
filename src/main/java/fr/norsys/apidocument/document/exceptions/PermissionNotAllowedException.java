package fr.norsys.apidocument.document.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class PermissionNotAllowedException extends Exception {
    public PermissionNotAllowedException(String message) {
        super(message);
    }
}
