package com.grinyov.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling of errors
 *
 * // TODO(partly processed) are all error kinds really bad requests?
 // What about if ID is incorrect? 404 not found should be returned.
 // What if script engine cannot be found due to different version of JRE? It is a kind of server error. Same with misconfigured or unavailable database connection..
 // NPEs, Out of memory errors and other runtime exceptions and ERRORS are also likely server errors (5xx)
 // but errors aren't handled yet
 *
 * @author vgrinyov
 */
@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidScriptStateException.class)
    public ResponseEntity<Object> scriptStateExceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ScriptNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<Object> notFoundExceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> serverSideExceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
