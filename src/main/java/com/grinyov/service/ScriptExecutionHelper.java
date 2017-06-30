package com.grinyov.service;

import com.grinyov.dao.ScriptRepository;
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

    @Autowired
    private ScriptRepository scriptRepository;


    public void executeScript(Script script) throws ExecutionException {

        ScriptEngine engine = engineManager.getEngine();

        if (!engineManager.compile(script.getScript(), engine)) {
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
            //script.setResult("The result of running the script: " +(String)engine.eval(script.getScript()));
            logger.info(script.getResult());
            scriptRepository.save(script);
        } catch (ScriptException e) {
            script.setStatus(Script.Status.FAILED);
            script.setResult("Failed to run the script: " + stringWriter);
            scriptRepository.save(script);
            logger.error("The script can not execute", e);
            throw new InvalidScriptStateException("script executed unsuccessful!");
        }

        script.setResult(stringWriter.toString());
        script.setStatus(Script.Status.DONE);
        scriptRepository.save(script);
        logger.info("script executed successful. Detail: " + script.getStatus() + ". Result:  " + script.getResult());
    }
}
