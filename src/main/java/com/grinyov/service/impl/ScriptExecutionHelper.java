package com.grinyov.service.impl;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.FailedScriptCompilationException;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgrinyov
 */
@Component
public class ScriptExecutionHelper {

    private static final Logger logger = Logger.getLogger(ScriptExecutionHelper.class);

    @Autowired
    private ScriptRepository scriptRepository;


    @Value("${engine.name}")
    private String engineName;

    public ScriptEngine getEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName(engineName);
        logger.debug("Engine was created");
        return engine;
    }

    public boolean compileScript(String script, ScriptEngine engine) {
        try {
            ((Compilable) engine).compile(script);
            logger.info("Script compiled successful: \n" + script);
            return true;
        } catch (ScriptException e) {
            logger.warn("Script \"" + script + "\" compiled unsuccessful!");
            return false;
        }
    }


    public void executeScript(Script script) throws ExecutionException {

        ScriptEngine engine = getEngine();

        if (!compileScript(script.getScript(), engine)) {
            logger.error("The script can not compile");
            throw new FailedScriptCompilationException("script compiled unsuccessful!");
        }

        StringWriter stringWriter = new StringWriter();
        engine.getContext().setWriter(stringWriter);

        try {
            script.setStatus(Script.Status.RUNNING);
            scriptRepository.save(script);
            engine.eval(script.getScript());
            logger.info("script " + script.getId() + " detail: " + script.getStatus());
            script.setResult("The result of running the script: " + stringWriter);
            logger.info(script.getResult());
            script.setResult(stringWriter.toString());
            script.setStatus(Script.Status.DONE);
            scriptRepository.save(script);
            logger.info("script executed successful. Detail: " + script.getStatus() + ". Result:  " + script.getResult());
        } catch (ScriptException e) {
            script.setStatus(Script.Status.FAILED);
            script.setResult("Failed to run the script: " + stringWriter);
            scriptRepository.save(script);
            logger.error("The script can not execute", e);
            throw new InvalidScriptStateException("script executed unsuccessful!");
        }

    }
}
