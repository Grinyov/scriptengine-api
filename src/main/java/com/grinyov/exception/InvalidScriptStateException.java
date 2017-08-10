package com.grinyov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Means that the script hung and does not respond
 *
 * @author vgrinyov
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidScriptStateException extends RuntimeException {
    public InvalidScriptStateException(String message) {
        super(message);
    }
}
