package com.grinyov.controller;


import com.grinyov.service.ScriptProccessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vgrinyov.
 */
@RepositoryRestController
public class ScriptResourceController {

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    @RequestMapping(value = "/scripts/{id}/running",
            method = RequestMethod.PUT, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource perform(@PathVariable("id") Long id,
                                            PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.perform(id));
    }

    @RequestMapping(value = "/scripts/{id}/status",
            method = RequestMethod.GET, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource viewOne(@PathVariable("id") Long id,
                                            PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.status(id));
    }

    @RequestMapping(value = "/scripts/{id}/terminate",
            method = RequestMethod.PUT, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource terminateOne(@PathVariable("id") Long id,
                                                 PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.terminate(id));
    }

     @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> exceptionHandler(Exception e) {
        Map<String,String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
