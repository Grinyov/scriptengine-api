package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * Created by vgrinyov on 25.06.17.
 */
public interface ThreadTaskExecutorService {
    void runTask(Script script);

    void terminateTask(Script script);
}
