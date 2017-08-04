package com.grinyov.service.impl;

import com.grinyov.service.ScriptService;
import com.grinyov.domain.Script;
import com.grinyov.repository.ScriptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Script.
 */
@Service
@Transactional
public class ScriptServiceImpl implements ScriptService{

    private final Logger log = LoggerFactory.getLogger(ScriptServiceImpl.class);

    private final ScriptRepository scriptRepository;

    public ScriptServiceImpl(ScriptRepository scriptRepository) {
        this.scriptRepository = scriptRepository;
    }

    /**
     * Save a script.
     *
     * @param script the entity to save
     * @return the persisted entity
     */
    @Override
    public Script save(Script script) {
        log.debug("Request to save Script : {}", script);
        return scriptRepository.save(script);
    }

    /**
     *  Get all the scripts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Script> findAll(Pageable pageable) {
        log.debug("Request to get all Scripts");
        return scriptRepository.findAll(pageable);
    }

    /**
     *  Get one script by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Script findOne(Long id) {
        log.debug("Request to get Script : {}", id);
        return scriptRepository.findOne(id);
    }

    /**
     *  Delete the  script by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Script : {}", id);
        scriptRepository.delete(id);
    }

    @Override
    public Script perform(Long id) {
        return null;
    }

    @Override
    public Script status(Long id) {
        return null;
    }

    @Override
    public Script terminate(Long id) {
        return null;
    }

    @Override
    public String viewBody(Long id) {
        return null;
    }

    @Override
    public String viewDetail(Long id) {
        return null;
    }
}
