package com.grinyov.service;

import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by vgrinyov
 */
public class ScriptExecutionHelper {

    private static final Logger LOG = Logger.getLogger(ScriptExecutionHelper.class);

    @Autowired
    private TaskExecutor executor;

    @Autowired
    private EngineManager engineManager;


    public Script executeScript(Script script){
        return null;
    }

    public Script terminateScript(Script script){
        return null;
    }
}
