package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * Created by vgrinyov.
 */

public interface ScriptProccessingService {

    Script perform(Long id);
    Script detail(Long id);
    Iterable<Script> showAll();
    void terminate(Long id);
}
