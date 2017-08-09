package com.grinyov.controller;

import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.exception.ScriptNotFoundException;
import com.grinyov.service.ScriptProccessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling of script
 *
 * @author vgrinyov
 */
@RepositoryRestController
public class ScriptResourceController {

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    
    
    /**
     * TODO implemented REST API interface is unnecessary complex and inconsistent
     * 1. Immediately create Script by POSTing to /scripts/ with plaintext containing script body
     * 2. getting /scripts/id/ and /scripts/id/status returns the same body, should remain only former
     * 3. getting script info as json in /scripts/ and /scripts/id should not return its body and result, those are returned by separate resources
     * 4. DELETE /scripts/id/ should also terminate if script is running
     * 5. /running should be named /run or /execute; it should check current status and synchronize on script instance to avoid starting and terminating simultaneously
     * and it should use POST because it is not idempotent, and return only status code, not body 
     * 6. see this link in requirements http://restcookbook.com/Resources/asynchroneous-operations/ what status code is returned in case of async operations
     * 7. Some responses may be cached on client (like script body). Some, like output (you named it result), should not be cached. Have a look at cache control and conditional http headers
     * 
     * 
     * 
     * Create resource for running script
     */

    @RequestMapping(value = "/scripts/{id}/running",
            method = RequestMethod.PUT, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource perform(@PathVariable("id") Long id,
                                            PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.perform(id));
    }

    /**
     * Create resource for checking status of script
     */

    /**
     * @param id
     * @param asm
     * @return
     */
    @RequestMapping(value = "/scripts/{id}/status",
            method = RequestMethod.GET, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource viewOne(@PathVariable("id") Long id,
                                            PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.status(id));
    }

    /**
     * Create resource for terminating script
     * TODO terminate resource should return non-successful response it is already terminated or not started, 404 if it does not exist, and should use
     * POST because it is not idempotent, it also likely should not return body, just status is enough
     *   
     */

    @RequestMapping(value = "/scripts/{id}/terminate",
            method = RequestMethod.PUT, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource terminateOne(@PathVariable("id") Long id,
                                                 PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.terminate(id));
    }

    // TODO are all error kinds really bad requests?
    // What about if ID is incorrect? 404 not found should be returned. 
    // What if script engine cannot be found due to different version of JRE? It is a kind of server error. Same with misconfigured or unavailable database connection..
    // NPEs, Out of memory errors and other runtime exceptions and ERRORS are also likely server errors (5xx)
    // but errors aren't handled yet
    
    @ExceptionHandler(InvalidScriptStateException.class)
    public ResponseEntity<Object> scriptStateExceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ScriptNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<Object> notFoundExceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }

}
