package com.grinyov.service;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by vgrinyov.
 */
public class ScriptProccessingServiceImpl implements ScriptProccessingService {

    @Autowired
    private ScriptRepository scriptRepository;

    @Override
    public Script perform(Long id) throws InvalidScriptStateException {
       Script script = scriptRepository.findOne(id);
       // run script in ScriptEngine
        return scriptRepository.save(script);
    }

    @Override
    public Script detail(Long id) {
        Script script = scriptRepository.findOne(id);
        // show script
        return scriptRepository.save(script);
    }

    @Override
    public void terminate(Long id) throws InvalidScriptStateException {
        Script script = scriptRepository.findOne(id);
        // terminate script
    }

}
