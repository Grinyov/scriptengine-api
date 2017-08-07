package com.grinyov.service.impl;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.exception.InvalidScriptStateException;
import com.grinyov.model.Script;
import com.grinyov.service.ScriptProccessingService;
import com.grinyov.service.ScriptThreadExecutorService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO the substantial problem with existing implementation is that state is not properly synchronized between persistent and in-memory state.  
 * Lets assume we restart our server while some scripts are in RUNNING state. Then, persistent repository will return them as RUNNING, but in fact in-memory state is lost, and there's 
 * no thread associated with the RUNNING script instance.  
 * 
 * Created by vgrinyov.
 */
@Service
@Transactional
public class ScriptProccessingServiceImpl implements ScriptProccessingService {

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ScriptThreadExecutorService executorService;


    private static final Logger logger = Logger.getLogger(ScriptProccessingServiceImpl.class);

    @Override
    public Script perform(Long id) throws InvalidScriptStateException {
        Script script = scriptRepository.findOne(id);
        executorService.runTask(script);
        // TODO what if next method fails and transaction rolls back? There will be no record in database but still a thread running script in memory
        return scriptRepository.save(script);
    }

    @Override
    // TODO consider marking read only transactional methods with read only transactional annotation
    public Script status(Long id) {
        Script script = scriptRepository.findOne(id);
        logger.info("script " + script.getId() +
                " status: " + script.getStatus());
        // TODO why do we need to save script here?
        // What if findOne cannot find script with such an id? We need to return 404 
        return scriptRepository.save(script);
    }

    @Override
    public Script terminate(Long id) throws InvalidScriptStateException {
        Script script = scriptRepository.findOne(id);
        // TODO what if there's no script with such an id?
        executorService.terminateTask(script);
        return scriptRepository.save(script);
    }

    // TODO make this a repository method which selects only script property, not the entire entity
    // TODO consider using in-memory cache of Scripts, this is faster than connecting to database
    @Override
    public String viewBody(Long id) {
        Script script = scriptRepository.findOne(id);
        return script.getBody();
    }

    // TODO make this a repository method which selects only result property, not the entire entity
    // TODO consider using in-memory cache of Scripts, this is faster than connecting to database
    @Override
    public String viewDetail(Long id) {
        Script script = scriptRepository.findOne(id);
        return script.getResult();
    }

}
