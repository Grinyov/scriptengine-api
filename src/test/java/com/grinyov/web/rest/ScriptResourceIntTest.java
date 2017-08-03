package com.grinyov.web.rest;

import com.grinyov.ScriptengineApiApp;

import com.grinyov.domain.Script;
import com.grinyov.repository.ScriptRepository;
import com.grinyov.service.ScriptService;
import com.grinyov.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.grinyov.domain.enumeration.Status;
/**
 * Test class for the ScriptResource REST controller.
 *
 * @see ScriptResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptengineApiApp.class)
public class ScriptResourceIntTest {

    private static final String DEFAULT_SCRIPT = "AAAAAAAAAA";
    private static final String UPDATED_SCRIPT = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.NEW;
    private static final Status UPDATED_STATUS = Status.RUNNING;

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    @Autowired
    private ScriptRepository scriptRepository;

    @Autowired
    private ScriptService scriptService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScriptMockMvc;

    private Script script;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScriptResource scriptResource = new ScriptResource(scriptService);
        this.restScriptMockMvc = MockMvcBuilders.standaloneSetup(scriptResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Script createEntity(EntityManager em) {
        Script script = new Script()
            .script(DEFAULT_SCRIPT)
            .status(DEFAULT_STATUS)
            .result(DEFAULT_RESULT);
        return script;
    }

    @Before
    public void initTest() {
        script = createEntity(em);
    }

    @Test
    @Transactional
    public void createScript() throws Exception {
        int databaseSizeBeforeCreate = scriptRepository.findAll().size();

        // Create the Script
        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(script)))
            .andExpect(status().isCreated());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeCreate + 1);
        Script testScript = scriptList.get(scriptList.size() - 1);
        assertThat(testScript.getScript()).isEqualTo(DEFAULT_SCRIPT);
        assertThat(testScript.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testScript.getResult()).isEqualTo(DEFAULT_RESULT);
    }

    @Test
    @Transactional
    public void createScriptWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scriptRepository.findAll().size();

        // Create the Script with an existing ID
        script.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(script)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkScriptIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptRepository.findAll().size();
        // set the field null
        script.setScript(null);

        // Create the Script, which fails.

        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(script)))
            .andExpect(status().isBadRequest());

        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = scriptRepository.findAll().size();
        // set the field null
        script.setStatus(null);

        // Create the Script, which fails.

        restScriptMockMvc.perform(post("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(script)))
            .andExpect(status().isBadRequest());

        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScripts() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);

        // Get all the scriptList
        restScriptMockMvc.perform(get("/api/scripts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(script.getId().intValue())))
            .andExpect(jsonPath("$.[*].script").value(hasItem(DEFAULT_SCRIPT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())));
    }

    @Test
    @Transactional
    public void getScript() throws Exception {
        // Initialize the database
        scriptRepository.saveAndFlush(script);

        // Get the script
        restScriptMockMvc.perform(get("/api/scripts/{id}", script.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(script.getId().intValue()))
            .andExpect(jsonPath("$.script").value(DEFAULT_SCRIPT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingScript() throws Exception {
        // Get the script
        restScriptMockMvc.perform(get("/api/scripts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScript() throws Exception {
        // Initialize the database
        scriptService.save(script);

        int databaseSizeBeforeUpdate = scriptRepository.findAll().size();

        // Update the script
        Script updatedScript = scriptRepository.findOne(script.getId());
        updatedScript
            .script(UPDATED_SCRIPT)
            .status(UPDATED_STATUS)
            .result(UPDATED_RESULT);

        restScriptMockMvc.perform(put("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScript)))
            .andExpect(status().isOk());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeUpdate);
        Script testScript = scriptList.get(scriptList.size() - 1);
        assertThat(testScript.getScript()).isEqualTo(UPDATED_SCRIPT);
        assertThat(testScript.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testScript.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingScript() throws Exception {
        int databaseSizeBeforeUpdate = scriptRepository.findAll().size();

        // Create the Script

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restScriptMockMvc.perform(put("/api/scripts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(script)))
            .andExpect(status().isCreated());

        // Validate the Script in the database
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteScript() throws Exception {
        // Initialize the database
        scriptService.save(script);

        int databaseSizeBeforeDelete = scriptRepository.findAll().size();

        // Get the script
        restScriptMockMvc.perform(delete("/api/scripts/{id}", script.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Script> scriptList = scriptRepository.findAll();
        assertThat(scriptList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Script.class);
        Script script1 = new Script();
        script1.setId(1L);
        Script script2 = new Script();
        script2.setId(script1.getId());
        assertThat(script1).isEqualTo(script2);
        script2.setId(2L);
        assertThat(script1).isNotEqualTo(script2);
        script1.setId(null);
        assertThat(script1).isNotEqualTo(script2);
    }
}
