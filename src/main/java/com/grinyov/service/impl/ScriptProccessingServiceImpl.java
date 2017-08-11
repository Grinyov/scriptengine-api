package com.grinyov.service.impl;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.exception.ScriptNotFoundException;
import com.grinyov.model.Script;
import com.grinyov.model.Status;
import com.grinyov.service.ScriptProccessingService;
import com.grinyov.util.ScriptEventHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * TODO the substantial problem with existing implementation is that state is not properly synchronized between persistent and in-memory state.  
 * Lets assume we restart our server while some scripts are in RUNNING state. Then, persistent repository will return them as RUNNING, but in fact in-memory state is lost, and there's 
 * no thread associated with the RUNNING script instance.  
 * 
 * Created by vgrinyov.
 */
@Service
@Transactional
public class ScriptProccessingServiceImpl implements ScriptProccessingService {

    @Autowired
    private ScriptRepository scriptRepository;

    private Map<Long, Thread> tasks = new ConcurrentHashMap<>();

    private ScriptContext context;

    private static final Logger logger = Logger.getLogger(ScriptProccessingServiceImpl.class);

    private void executeScript(Script script) throws ExecutionException {
        StringWriter writer = new StringWriter();
        context = new SimpleScriptContext();
        context.setWriter(writer);
        try {
            script.setStatus(Status.RUNNING);
            // TODO why calling save so much times? Read JPA/Hibernate doc about how and when entity is saved
            scriptRepository.save(script);
            logger.info(script.getStatus());
            script.getCompiledScript().eval(context);
            // TODO result is not saved during script execution, as it was requested, only after script completion
            script.setResult("The result of running the script: " + writer.getBuffer());
            logger.info(script.getResult());
            script.setStatus(Status.DONE);
            // TODO why calling save so much times? Read JPA/Hibernate doc about how and when entity is saved
            scriptRepository.save(script);
            logger.info("script executed successful. Status: " + script.getStatus() + ". Detail:  " + script.getResult());
        } catch (ScriptException e) {
            script.setStatus(Status.FAILED);
            script.setResult("Failed to run the script: " + writer.getBuffer());
            // TODO why calling save so much times? Read JPA/Hibernate doc about how and when entity is saved
            scriptRepository.save(script);
            logger.error("The script can not execute", e);
            // TODO important error information is lost, including stack trace!!!
            throw new InvalidScriptStateException("script executed unsuccessful!. Detail: " + e.getMessage());
        }
    }

    @Override
    public Script perform(Long id) throws InvalidScriptStateException {
        Script script = scriptRepository.findOne(id);
        if (script == null){
            throw new ScriptNotFoundException("Script with id: " + id +  " not found in database" );
        }
        // TODO what if next method fails and transaction rolls back? There will be no record in database but still a thread running script in memory
         Runnable runnable = () -> {
            /*// TODO the below logic is meaningless? Or I don't understand its purpose*/
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    logger.info("thread " + Thread.currentThread().getName() + " is running.");
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

        return scriptRepository.save(script);
    }

    @Override
    // TODO(processed) consider marking read only transactional methods with read only transactional annotation
    @Transactional(readOnly = true, propagation= Propagation.SUPPORTS)
    public Script status(Long id) {
        Script script = scriptRepository.findOne(id);
        if (script == null){
            throw new ScriptNotFoundException("Script with id: " + id +  " not found in database" );
        }
        logger.info("script " + script.getId() +
                " status: " + script.getStatus());
        // TODO(processed) why do we need to save script here?
        // (processed) What if findOne cannot find script with such an id? We need to return 404
        return script;
    }

    @Override
    public Script terminate(Long id) {
        Script script = scriptRepository.findOne(id);
        // TODOprocessed) what if there's no script with such an id?
        if (script == null || script.getStatus() != Status.RUNNING ){
            throw new ScriptNotFoundException("Script with id: " + id +  " not running" );
        }
        Thread currentThread = tasks.get(script.getId());
        currentThread.interrupt();
        // TODO(processed) task may not be terminated
        logger.info("the task running in " + currentThread.getName() + " trying stop. ");
        tasks.remove(script.getId());
        // TODO(processed) is status FAILED or TERMINATED ? FAILED means script completed due to its runtime exception
        try {
            currentThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.warn("thread " + currentThread.getName() + " was interrupted. Detail: " + e.getMessage());
            currentThread.interrupt();
        }
        if (currentThread.isAlive()) {
            currentThread.stop();
            logger.warn("Thread was force stopped");
        }
        logger.info("thread " + currentThread.getName() + " was stopped.");
        script.setStatus(Status.TERMINATED);
        /*try {
            // TODO(processed) meaningless
            currentThread.wait(500);
        } catch (InterruptedException e) {
            // TODO(processed) InterruptedException may happened above try
            // TODO(processed) Why do we throw this exception if it is an expected behavior?
            throw new InvalidScriptStateException(e.getMessage());
        } finally {
            // TODO(processed) why this part is in finally block?
            if (currentThread.isAlive()) {
                currentThread.stop();
                logger.info("Thread was force stopped");
            }
        }*/
        return scriptRepository.save(script);
    }

    // TODO(processed) make this a repository method which selects only script property, not the entire entity
    // TODO(processed) consider using in-memory cache of Scripts, this is faster than connecting to database
    @Override
    @Cacheable(cacheNames="scripts", key = "{#root.method, #id}", sync = true)
    public String viewBody(Long id) {
        return scriptRepository.findBodyById(id);
    }

    // TODO(processed) make this a repository method which selects only result property, not the entire entity
    // TODO(processed) consider using in-memory cache of Scripts, this is faster than connecting to database
    @Override
    @Cacheable(cacheNames="scripts", key = "{#root.method, #id}", sync = true)
    public String viewResult(Long id) {
        return scriptRepository.findResultById(id);
    }

}
