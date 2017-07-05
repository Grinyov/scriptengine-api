package com.grinyov.service.impl;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.FailedScriptCompilationException;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import com.grinyov.service.ScriptThreadExecutorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.script.Compilable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.*;


/**
 * Created by vgrinyov
 */
@Service
public class ScriptThreadExecutorServiceImpl implements ScriptThreadExecutorService {

    private static final Logger logger = Logger.getLogger(ScriptThreadExecutorServiceImpl.class);

    @Value("${timeout}")
    private int timeout;

    @Value("${engine.name}")
    private String engineName;

    private Map<Long, ExecutorService> executors = new ConcurrentHashMap<>();

    @Autowired
    private ScriptRepository scriptRepository;


    public ScriptEngine getEngine() {
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName(engineName);
        logger.debug("Engine was created");
        return engine;
    }

    public boolean compileScript(String script, ScriptEngine engine) {
        try {
            ((Compilable) engine).compile(script);
            logger.debug("Script compiled successful. :-) \n");
            return true;
        } catch (ScriptException e) {
            logger.warn("Script \"" + script + "\" compiled unsuccessful. :-(");
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
//        engine.getContext().setWriter(stringWriter);

        try {
            engine.getContext().setWriter(stringWriter);
            script.setStatus(Script.Status.RUNNING);
            scriptRepository.save(script);
            engine.eval(script.getScript());
            logger.info("script " + script.getId() + " status: " + script.getStatus());
            //check 1
            script.setResult("The result of running the script: " + stringWriter);
            logger.info(script.getResult());
            script.setStatus(Script.Status.DONE);
            scriptRepository.save(script);
            logger.info("script executed successful. Status: " + script.getStatus() + ". Detail:  " + script.getResult());
        } catch (ScriptException e) {
            script.setStatus(Script.Status.FAILED);
            script.setResult("Failed to run the script: " + stringWriter);
            scriptRepository.save(script);
            logger.error("The script can not execute", e);
            throw new InvalidScriptStateException("script executed unsuccessful!");
        }

    }

    @Override
    public void runTask(Script script) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executors.put(script.getId(), executor);
        executor.submit(() -> {
            try {
                executeScript(script);
            } catch (ExecutionException e) {
                logger.error("script executed failed ", e);
            }
        });
    }

    @Override
    public void terminateTask(Script script) {
        ExecutorService executor = executors.get(script.getId());
        try {
            executor.awaitTermination(timeout, TimeUnit.SECONDS);
            logger.info("the task is terminated. " + Thread.currentThread() +
                    " is managed " + executor.toString() + " is shutdown!");
            executor.shutdown();
        } catch (InterruptedException e) {
            logger.error("script shutdown failed ", e);
        }
    }
}
