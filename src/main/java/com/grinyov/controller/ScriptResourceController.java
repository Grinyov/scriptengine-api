package com.grinyov.controller;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.model.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
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
    private ScriptRepository scriptRepository;

    @RequestMapping(value = "/scripts/{id}/running",
            method = RequestMethod.PUT)
    @ResponseBody
    public Resource running(@PathVariable("id") Long id,
                            PersistentEntityResourceAssembler asm){
        final Script script = scriptRepository.findOne(id);
        // some logic for service.run(ad)
        return asm.toFullResource(scriptRepository.save(script));
    }
}
