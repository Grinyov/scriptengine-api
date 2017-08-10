package com.grinyov.exception;

import org.apache.log4j.Logger;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLDataException;
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
    private static final Logger log = Logger.getLogger(ErrorController.class);

        /**
         * Catch all for any other exceptions...
         */
        @ExceptionHandler({ Exception.class })
        @ResponseBody
        public ResponseEntity<?> handleAnyException(Exception e) {
            return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        /**
         * Handle failures commonly thrown from code
         */
        @ExceptionHandler({ InvalidScriptStateException.class, FailedScriptCompilationException.class, InvocationTargetException.class, IllegalArgumentException.class, ClassCastException.class,
                ConversionFailedException.class})
        @ResponseBody
        public ResponseEntity handleMiscFailures(Throwable t) {
            return errorResponse(t, HttpStatus.BAD_REQUEST);
        }

        /**
         * Handle failures with resources thrown from code
         */
        @ExceptionHandler({ResourceNotFoundException.class, ScriptNotFoundException.class,  IllegalMonitorStateException.class})
        @ResponseBody
        public ResponseEntity handleCustomFailures(Throwable t) {
            return errorResponse(t, HttpStatus.NOT_FOUND);
        }


        protected ResponseEntity<ExceptionMessage> errorResponse(Throwable throwable,
                                                                 HttpStatus status) {
            if (null != throwable) {
                log.error("error caught: " + throwable.getMessage(), throwable);
                return response(new ExceptionMessage(throwable), status);
            } else {
                log.error("unknown error caught in API, {}");
                return response(null, status);
            }
        }

        protected <T> ResponseEntity<T> response(T body, HttpStatus status) {
            log.debug("Responding with a status of {}");
            return new ResponseEntity<>(body, new HttpHeaders(), status);
        }

//    @ExceptionHandler(value = {NullPointerException.class, SQLDataException.class, DataAccessException.class, IllegalMonitorStateException.class})
//    public ResponseEntity<Object> serverSideExceptionHandler(Exception e) {
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("message", e.getMessage());
//        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
