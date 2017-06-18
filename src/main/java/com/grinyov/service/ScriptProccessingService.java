package com.grinyov.service;

import com.grinyov.model.Script;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vgrinyov.
 */
@Service
@Transactional
public interface ScriptProccessingService {

    Script perform(Long id);
    Script detail(Long id);
    void terminate(Long id);
}
