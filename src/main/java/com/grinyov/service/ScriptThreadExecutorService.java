package com.grinyov.service;

import com.grinyov.model.Script;
import org.springframework.stereotype.Service;

/**
 * The service for starting and Interrupting threads
 *
 * @author vgrinyov
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
     * !!! Warning this method used depricated mechanism stopping thread
     *
     * @param script
     */
    void terminateTask(Script script);
}
