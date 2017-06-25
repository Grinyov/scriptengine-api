package com.grinyov.service;

import com.grinyov.exception.FailedScriptCompilationException;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.script.ScriptEngine;
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
    private EngineManager engineManager;


    public void executeScript(Script script) throws ExecutionException {

        ScriptEngine engine = engineManager.getEngine();

        if (!engineManager.compile(script.getScript(), engine)) {
            throw new FailedScriptCompilationException("script compiled unsuccessful!");
        }

        StringWriter stringWriter = new StringWriter();
        engine.getContext().setWriter(stringWriter);

        try {
            engine.eval(script.getScript());
            script.setStatus(Script.Status.RUNNING);
            logger.info("script " + script.getId() + "running");
        } catch (ScriptException e) {
            logger.info("script executed unsuccessful!");
            logger.info(e.getMessage());
            throw new InvalidScriptStateException("script executed unsuccessful!");
        }
        script.setStatus(Script.Status.DONE);
        logger.info("script executed successful");
    }
}
