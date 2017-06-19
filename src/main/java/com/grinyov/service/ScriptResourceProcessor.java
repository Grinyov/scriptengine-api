package com.grinyov.service;

import com.grinyov.controller.ScriptResourceController;
import com.grinyov.model.Script;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by vgrinyov.
 */
@Component
public class ScriptResourceProcessor implements ResourceProcessor<Resource<Script>> {

    @Autowired
    private RepositoryEntityLinks entityLinks;

    @Override
    public Resource<Script> process(Resource<Script> resource) {
        final Script script = resource.getContent();
        if (script.getStatus() != Script.Status.RUNNING){
            resource.add(linkTo(methodOn(ScriptResourceController.class).perform(script.getId(), null)).withRel("running"));
        }

        resource.add(entityLinks.linkToSingleResource(script).withRel("terminate"));
//        if (script.getStatus() == Script.Status.RUNNING){
//            resource.add(linkTo(methodOn(ScriptResourceController.class).viewAll().withRel("showAll");
//        }

        return resource;
    }
}
