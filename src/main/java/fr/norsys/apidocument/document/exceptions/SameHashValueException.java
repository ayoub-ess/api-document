package fr.norsys.apidocument.document.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class SameHashValueException extends Exception {
    public SameHashValueException(String file_already_exists) {
        super(file_already_exists);
    }
}
