package com.grinyov.exception;


/**
 * Created by vgrinyov
 */
public class FailedScriptCompilationException extends RuntimeException {
    public FailedScriptCompilationException(String message) {
        super(message);
    }
}
