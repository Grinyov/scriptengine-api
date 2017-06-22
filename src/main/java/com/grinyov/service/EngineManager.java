package com.grinyov.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by vgrinyov
 */
@Component
@PropertySource("classpath:application.properties")
public class EngineManager {

    private static final Logger LOG = Logger.getLogger(EngineManager.class);

    @Value("${engine.name}")
    private String engineName;

    public ScriptEngine getEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName(engineName);
        LOG.debug("Engine was created");
        return engine;
    }

    public boolean compile(String script, ScriptEngine engine) {
        try {
            ((Compilable) engine).compile(script);
            LOG.debug("Script compiled successful: \n" + script);
            return true;
        } catch (ScriptException e) {
            LOG.warn("Script \"" + script + "\" compiled unsuccessful!");
            return false;
        }
    }
}
