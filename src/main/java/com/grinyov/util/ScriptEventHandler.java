package com.grinyov.util;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.event.ScriptLaunched;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import com.grinyov.service.ScriptProccessingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Handler different events in the application
 *
 * @author vgrinyov
 */
@Component
@RepositoryEventHandler
public class ScriptEventHandler {

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    @Autowired
    private ScriptRepository scriptRepository;

    private static final Logger logger = Logger.getLogger(Script.class);


    private ScriptEngine getEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("nashorn");
        logger.debug("Engine was created");
        return engine;
    }

    /**
     * Handle created scripts and validate them
     */
    @HandleBeforeCreate
    public void scriptValidating(Script script) {
        if (!script.compileScript(script.getBody(), getEngine())){
            logger.warn("Script \"" + script.getBody() + "\" compiled unsuccessful");
            throw new InvalidScriptStateException("compiled unsuccessful");
        } 
    }
    /**
     * Handle created scripts and run them
     */
    @HandleAfterCreate
    public void scriptRunning(Script script) {
        scriptProccessingService.perform(script.getId());
    }

    /**
     * Handle launched scripts and add task of writing result to scheduler
     */
    @EventListener
    public void scriptResultWriting(ScriptLaunched scriptLaunched){
        Long id = scriptLaunched.getEventData();
        logger.info("script with id: " + id + " launched");
        // run  task for script with id
    }
}
