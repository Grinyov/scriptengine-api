package com.grinyov.service;

import com.grinyov.domain.Script;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Script.
 */
public interface ScriptService {

    /**
     * Save a script.
     *
     * @param script the entity to save
     * @return the persisted entity
     */
    Script save(Script script);

    /**
     *  Get all the scripts.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Script> findAll(Pageable pageable);

    /**
     *  Get the "id" script.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Script findOne(Long id);

    /**
     *  Delete the "id" script.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Starts processing of specified script
     *
     * @param id
     * @return script
     */
    Script perform(Long id);

    /**
     * Shows status of script
     *
     * @param id
     * @return script
     */
    Script status(Long id);

    /**
     * Interrupts the processing of this script
     *
     * @param id
     * @return script
     */
    Script terminate(Long id);

    /**
     * Views the body of script
     *
     * @param id
     * @return script's body as plain text
     */
    String viewBody(Long id);

    /**
     * Views the detail of running the script
     *
     * @param id
     * @return result of running the script as plain text
     */
    String viewDetail(Long id);
}
