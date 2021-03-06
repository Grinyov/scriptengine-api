package com.grinyov.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Means that the compilation failed
 *
 * @author vgrinyov
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class FailedScriptCompilationException extends RuntimeException {
    public FailedScriptCompilationException(String message) {
        super(message);
    }
}
