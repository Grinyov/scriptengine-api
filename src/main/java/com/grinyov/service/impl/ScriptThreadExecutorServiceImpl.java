package com.grinyov.service.impl;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import com.grinyov.model.Status;
import com.grinyov.service.ScriptThreadExecutorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;


/**
 * Created by vgrinyov
 */
@Service
public class ScriptThreadExecutorServiceImpl implements ScriptThreadExecutorService {

    private static final Logger logger = Logger.getLogger(ScriptThreadExecutorServiceImpl.class);

    @Value("${timeout}")
    private int timeout;

    // TODO(processed) unused, remove
    private Map<Long, Thread> tasks = new ConcurrentHashMap<>();
    // TODO(processed) unused, remove


    @Autowired
    private ScriptRepository scriptRepository;

    private void executeScript(Script script) throws ExecutionException {
        StringWriter stringWriter = new StringWriter();
        try {
            script.setStatus(Status.RUNNING);
            // TODO why calling save so much times? Read JPA/Hibernate doc about how and when entity is saved 
            scriptRepository.save(script);
            logger.info(script.getStatus());
            script.getCompiledScript().eval();
            // TODO result is not saved during script execution, as it was requested, only after script completion
            script.setResult("The result of running the script: " + stringWriter);
            logger.info(script.getResult());
            script.setStatus(Status.DONE);
            // TODO why calling save so much times? Read JPA/Hibernate doc about how and when entity is saved 
            scriptRepository.save(script);
            logger.info("script executed successful. Status: " + script.getStatus() + ". Detail:  " + script.getResult());
        } catch (ScriptException e) {
            script.setStatus(Status.FAILED);
            script.setResult("Failed to run the script: " + stringWriter);
            // TODO why calling save so much times? Read JPA/Hibernate doc about how and when entity is saved 
            scriptRepository.save(script);
            logger.error("The script can not execute", e);
            // TODO important error information is lost, including stack trace!!!
            throw new InvalidScriptStateException("script executed unsuccessful!");
        }

    }

    @Override
    public void runTask(Script script) {

        Runnable runnable = () -> {
            // TODO the below logic is meaningless? Or I don't understand its purpose
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    executeScript(script);
                    return;
                    // TODO when execution exception can be thrown? In what thread?
                } catch (ExecutionException e) {
                    logger.error("script executed failed ", e);
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        tasks.put(script.getId(), thread);
        // TODO(processed) why do we need an executor here?
    }

    @Override
    public void terminateTask(Script script) {

        Thread currentThread = tasks.get(script.getId());
        currentThread.interrupt();
        // TODO task may not be terminated
        logger.info("the task is terminated. " + currentThread.getName() +
                " is shutdown!");
        tasks.remove(script.getId());
        // TODO is status FAILED or TERMINATED ? FAILED means script completed due to its runtime exception
        script.setStatus(Status.FAILED);
        scriptRepository.save(script);
        try {
            // TODO meaningless
            currentThread.wait(500);
        } catch (InterruptedException e) {
            // TODO InterruptedException may happened above try
            // TODO Why do we throw this exception if it is an expected behavior?
            throw new InvalidScriptStateException(e.getMessage());
        } finally {
            // TODO why this part is in finally block?
            if (currentThread.isAlive()) {
                currentThread.stop();
                logger.info("Thread was force stopped");
            }
        }
    }

    // TODO why exception handler here? It duplicates {@link ScriptResourceController#exceptionHandler}
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> exceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
