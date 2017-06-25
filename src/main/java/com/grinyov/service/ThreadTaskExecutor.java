package com.grinyov.service;

import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;


/**
 * Created by vgrinyov
 *
 */
@Service
public class ThreadTaskExecutor implements ThreadTaskExecutorService {

    private static final Logger logger = Logger.getLogger(ScriptExecutionHelper.class);

    private Map<Long, ExecutorService> executors= new ConcurrentHashMap<>();

    @Autowired
    private ScriptExecutionHelper scriptExecutionHelper;

    @Override
    public void runTask(Script script){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executors.put(script.getId(), executor);
        executor.submit(() -> {
            try {
                scriptExecutionHelper.executeScript(script);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void terminateTask(Script script){
        ExecutorService executor = executors.get(script.getId());
        executor.shutdownNow();
    }
}
