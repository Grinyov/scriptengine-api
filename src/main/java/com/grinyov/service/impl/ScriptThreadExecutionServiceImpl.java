package com.grinyov.service.impl;

import com.grinyov.domain.Script;
import com.grinyov.domain.enumeration.Status;
import com.grinyov.repository.ScriptRepository;
import com.grinyov.service.ScriptThreadExecutionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @author vgrinyov
 */
@Service
public class ScriptThreadExecutionServiceImpl implements ScriptThreadExecutionService {

    private static final Logger log = Logger.getLogger(ScriptThreadExecutionServiceImpl.class);
    private Map<Long, Thread> tasks = new ConcurrentHashMap<>();

    @Autowired
    private ScriptRepository scriptRepository;

    private void executeScript(Script script) throws ExecutionException {
        StringWriter stringWriter = new StringWriter();
        try {
            script.setStatus(Status.RUNNING);
            scriptRepository.save(script);
            log.info(script.getStatus());
            script.getCompiledScript().eval();
            script.setResult("The result of running the script: " + stringWriter);
            log.info(script.getResult());
            script.setStatus(Status.DONE);
            scriptRepository.save(script);
            log.info("script executed successful. Status: " + script.getStatus() + ". Detail:  " + script.getResult());
        } catch (ScriptException e) {
            script.setStatus(Status.FAILED);
            script.setResult("Failed to run the script: " + stringWriter);
            scriptRepository.save(script);
            log.error("The script can not execute", e);
            //throw new InvalidScriptStateException("script executed unsuccessful!");
        }

    }

    @Override
    public void runTask(Script script) {
        Runnable runnable = () -> {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    executeScript(script);
                    return;
                } catch (ExecutionException e) {
                    log.error("script executed failed ", e);
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        tasks.put(script.getId(), thread);
    }

    @Override
    public void terminateTask(Script script) {
        Thread currentThread = tasks.get(script.getId());
        currentThread.interrupt();
        log.info("the task is terminated. " + currentThread.getName() +
            " is shutdown!");
        tasks.remove(script.getId());
        script.setStatus(Status.FAILED);
        scriptRepository.save(script);
        try {
            currentThread.wait(500);
        } catch (InterruptedException e) {
            //throw new InvalidScriptStateException(e.getMessage());
        } finally {
            if (currentThread.isAlive()) {
                currentThread.stop();
                log.info("Thread was force stopped");
            }
        }
    }
}
