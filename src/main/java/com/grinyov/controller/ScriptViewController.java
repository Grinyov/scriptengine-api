package com.grinyov.controller;

import com.grinyov.service.ScriptProccessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by vgrinyov
 */
@Controller
public class ScriptViewController {

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    @RequestMapping(value = "/scripts/{id}/body",
            method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public String viewScriptBody(@PathVariable("id") Long id){
        return scriptProccessingService.viewBody(id);
    }

    @RequestMapping(value = "/scripts/{id}/detail",
            method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public String viewScriptDetail(@PathVariable("id") Long id){
        return scriptProccessingService.viewDetail(id);
    }
}
