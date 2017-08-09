package com.grinyov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Means that the script not found in database
 *
 * @author vgrinyov
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ScriptNotFoundException extends RuntimeException{
    public ScriptNotFoundException(String message) {
        super(message);
    }
}
