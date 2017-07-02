package com.grinyov.service.impl;

import com.grinyov.model.Script;
import com.grinyov.service.ThreadTaskExecutorService;
import com.grinyov.service.impl.ScriptExecutionHelper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;


/**
 * Created by vgrinyov
 */
@Service
public class ThreadTaskExecutor implements ThreadTaskExecutorService {

    private static final Logger logger = Logger.getLogger(ScriptExecutionHelper.class);

    @Value("${timeout}")
    private int timeout;

    private Map<Long, ExecutorService> executors = new ConcurrentHashMap<>();

    @Autowired
    private ScriptExecutionHelper scriptExecutionHelper;

    @Override
    public void runTask(Script script) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executors.put(script.getId(), executor);
        executor.submit(() -> {
            try {
                scriptExecutionHelper.executeScript(script);
                logger.info(script.getResult());
                executor.awaitTermination(timeout, TimeUnit.SECONDS);
                executor.shutdown();
            } catch (ExecutionException | InterruptedException e) {
                logger.error("script executed failed ", e);
            } finally {
                logger.info("the task is terminated. " + Thread.currentThread() + " is managed " + executor.toString() + " is shutdown!");
                executor.shutdownNow();
            }
        });
    }

    @Override
    public void terminateTask(Script script) {
        ExecutorService executor = executors.get(script.getId());
        executor.shutdownNow();
    }
}
