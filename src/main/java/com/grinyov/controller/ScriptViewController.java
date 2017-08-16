package com.grinyov.controller;

import com.grinyov.service.ScriptProccessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * Controller for viewing of script
 *
 * @author vgrinyov
 */
@Controller
public class ScriptViewController {

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    /**
     * Create resource for viewing body of script
     */

    @RequestMapping(value = "/scripts/{id}/body",
            method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public String viewScriptBody(@PathVariable("id") Long id) {
        return scriptProccessingService.viewBody(id);
    }

    /**
     * TODO(processed) why is it named /detail, not result (inconsistent naming)?
     * Create resource for viewing result of performing script
     */

    @RequestMapping(value = "/scripts/{id}/result",
            method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public String viewScriptDetail(@PathVariable("id") Long id) {
        return scriptProccessingService.viewResult(id);
    }
}
