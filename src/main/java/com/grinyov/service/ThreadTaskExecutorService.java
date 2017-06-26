package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * Created by vgrinyov
 */
public interface ThreadTaskExecutorService {
    void runTask(Script script);

    void terminateTask(Script script);
}
