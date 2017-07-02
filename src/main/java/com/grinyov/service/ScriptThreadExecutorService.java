package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * Created by vgrinyov
 * <p>
 * The service for starting and Interrupting threads
 */
public interface ScriptThreadExecutorService {

    /**
     * Starts script processing in a separate thread
     *
     * @param script
     */
    void runTask(Script script);

    /**
     * Interrupts thread
     *
     * @param script
     */
    void terminateTask(Script script);
}
