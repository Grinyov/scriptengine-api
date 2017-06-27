package com.grinyov.exception;


/**
 * Created by vgrinyov
 *
 * Means that the compilation failed
 */
public class FailedScriptCompilationException extends RuntimeException {
    public FailedScriptCompilationException(String message) {
        super(message);
    }
}
