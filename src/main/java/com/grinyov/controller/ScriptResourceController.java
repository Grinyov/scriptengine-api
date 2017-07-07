package com.grinyov.controller;


import com.grinyov.service.ScriptProccessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = "/scripts/{id}/detail",
            method = RequestMethod.GET, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource viewOne(@PathVariable("id") Long id,
                                            PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.detail(id));
    }

    @RequestMapping(value = "/scripts/{id}/terminate",
            method = RequestMethod.PUT, produces = "application/hal+json")
    @ResponseBody
    public PersistentEntityResource terminateOne(@PathVariable("id") Long id,
                                                 PersistentEntityResourceAssembler asm) {
        return asm.toFullResource(scriptProccessingService.terminate(id));
    }

}
