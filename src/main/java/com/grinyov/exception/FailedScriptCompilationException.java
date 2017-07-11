package com.grinyov.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by vgrinyov
 *
 * Means that the compilation failed
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class FailedScriptCompilationException extends RuntimeException {
    public FailedScriptCompilationException(String message) {
        super(message);
    }
}
