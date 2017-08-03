package com.grinyov.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.grinyov.domain.Script;
import com.grinyov.service.ScriptService;
import com.grinyov.web.rest.util.HeaderUtil;
import com.grinyov.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Script.
 */
@RestController
@RequestMapping("/api")
public class ScriptResource {

    private final Logger log = LoggerFactory.getLogger(ScriptResource.class);

    private static final String ENTITY_NAME = "script";

    private final ScriptService scriptService;

    public ScriptResource(ScriptService scriptService) {
        this.scriptService = scriptService;
    }

    /**
     * POST  /scripts : Create a new script.
     *
     * @param script the script to create
     * @return the ResponseEntity with status 201 (Created) and with body the new script, or with status 400 (Bad Request) if the script has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scripts")
    @Timed
    public ResponseEntity<Script> createScript(@Valid @RequestBody Script script) throws URISyntaxException {
        log.debug("REST request to save Script : {}", script);
        if (script.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new script cannot already have an ID")).body(null);
        }
        Script result = scriptService.save(script);
        return ResponseEntity.created(new URI("/api/scripts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scripts : Updates an existing script.
     *
     * @param script the script to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated script,
     * or with status 400 (Bad Request) if the script is not valid,
     * or with status 500 (Internal Server Error) if the script couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scripts")
    @Timed
    public ResponseEntity<Script> updateScript(@Valid @RequestBody Script script) throws URISyntaxException {
        log.debug("REST request to update Script : {}", script);
        if (script.getId() == null) {
            return createScript(script);
        }
        Script result = scriptService.save(script);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, script.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scripts : get all the scripts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scripts in body
     */
    @GetMapping("/scripts")
    @Timed
    public ResponseEntity<List<Script>> getAllScripts(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Scripts");
        Page<Script> page = scriptService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scripts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scripts/:id : get the "id" script.
     *
     * @param id the id of the script to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the script, or with status 404 (Not Found)
     */
    @GetMapping("/scripts/{id}")
    @Timed
    public ResponseEntity<Script> getScript(@PathVariable Long id) {
        log.debug("REST request to get Script : {}", id);
        Script script = scriptService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(script));
    }

    /**
     * DELETE  /scripts/:id : delete the "id" script.
     *
     * @param id the id of the script to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scripts/{id}")
    @Timed
    public ResponseEntity<Void> deleteScript(@PathVariable Long id) {
        log.debug("REST request to delete Script : {}", id);
        scriptService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
