package com.grinyov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by vgrinyov.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidScriptStateException extends RuntimeException {
    public InvalidScriptStateException(String message) {
        super(message);
    }
}
