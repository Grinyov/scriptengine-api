package com.grinyov.service;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by vgrinyov.
 */
@Service
@Transactional
public class ScriptProccessingServiceImpl implements ScriptProccessingService {

    @Autowired
    private ScriptRepository scriptRepository;

    private static final Logger logger = Logger.getLogger(ScriptProccessingServiceImpl.class);

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
    public Script terminate(Long id) throws InvalidScriptStateException {
        Script script = scriptRepository.findOne(id);
        // terminate script
        return scriptRepository.save(script);
    }



}
