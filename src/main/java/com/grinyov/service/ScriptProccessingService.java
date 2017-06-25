package com.grinyov.service;

import com.grinyov.model.Script;

/**
 * Created by vgrinyov.
 */

public interface ScriptProccessingService {

    Script perform(Long id);
    String detail(Long id);
    Script terminate(Long id);
}
