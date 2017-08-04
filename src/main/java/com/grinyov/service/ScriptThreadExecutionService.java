package com.grinyov.service;

import com.grinyov.domain.Script;

/**
 * The service for starting and Interrupting threads
 *
 * @author vgrinyov
 */
public interface ScriptThreadExecutionService {
    /**
     * Starts script processing in a separate thread
     *
     * @param script
     */
    void runTask(Script script);

    /**
     * Interrupts thread
     * !!! Warning this method used depricated mechanism stopping thread
     *
     * @param script
     */
    void terminateTask(Script script);
}
