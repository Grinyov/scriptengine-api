package com.grinyov.service.impl;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import com.grinyov.service.ScriptThreadExecutorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.StringWriter;
import java.util.HashMap;
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

    private Map<Long, ExecutorService> executors = new ConcurrentHashMap<>();
    private Map<Long, Thread> tasks = new ConcurrentHashMap<>();
    private Map<Long, Future> futures = new ConcurrentHashMap<>();

    @Autowired
    private ScriptRepository scriptRepository;

    private void executeScript(Script script) throws ExecutionException {
        StringWriter stringWriter = new StringWriter();
        try {
            script.setStatus(Script.Status.RUNNING);
            scriptRepository.save(script);
            logger.info(script.getStatus());
            script.getCompiledScript().eval();
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

        Runnable runnable = () -> {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    executeScript(script);
                    return;
                } catch (ExecutionException e) {
                    logger.error("script executed failed ", e);
                    Thread.currentThread().interrupt();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        tasks.put(script.getId(), thread);

        ExecutorService executor = Executors.newWorkStealingPool();
        executors.put(script.getId(), executor);

        /**
         *  snippet for blocking call
         */

       /* executor.submit(() -> {
            try {
                executeScript(script);
            } catch (ExecutionException e) {
                logger.error("script executed failed ", e);
                throw new InvalidScriptStateException(e.getMessage());
            }
        });*/

        /**
         *   snippet non-blocking call
         */

        /*   Future<String> future = executor.submit(() -> {
            try {
                executeScript(script);
                TimeUnit.SECONDS.sleep(1);
                return "";
            } catch (InterruptedException e) {
                throw new InvalidScriptStateException("task interrupted" + e);
            }
        });
        futures.put(script.getId(), future);
        try {
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new InvalidScriptStateException("task interrupted" + e);
        }*/

    }

    @Override
    public void terminateTask(Script script) {

        Thread currentThread = tasks.get(script.getId());
        currentThread.interrupt();
        logger.info("the task is terminated. " + currentThread.getName() +
                " is shutdown!");
        tasks.remove(script.getId());
        script.setStatus(Script.Status.FAILED);
        scriptRepository.save(script);
        try {
            currentThread.wait(500);
        } catch (InterruptedException e) {
            throw new InvalidScriptStateException(e.getMessage());
        } finally {
            if (currentThread.isAlive()) {
                currentThread.stop();
                logger.info("Thread was force stopped");
            }
        }

        /**
         *  snippet for blocking call
         */
      /*  ExecutorService executor = executors.get(script.getId());
        try {
            executor.shutdown();
            logger.info("the task is terminated. " + Thread.currentThread() +
                    " is managed " + executor.toString() + " is shutdown!");
            executor.awaitTermination(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("script shutdown failed ", e);
            throw new InvalidScriptStateException(e.getMessage());
        } finally {
            if (!executor.isShutdown()) {
                executor.shutdownNow();
            }
            script.setStatus(Script.Status.FAILED);
            executors.remove(script.getId());
            scriptRepository.save(script);
        }*/

        /**
         *   snippet non-blocking call
         */
        /*Future future = futures.get(script.getId());
        while (future.isCancelled()) {
            future.cancel(false);
        }*/
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> exceptionHandler(Exception e) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", e.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
    }
}
