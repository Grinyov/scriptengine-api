package com.grinyov.dao;

import com.grinyov.ScriptengineApiApplication;
import com.grinyov.model.Script;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;

/**
 * Created by vgrinyov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptengineApiApplication.class)
@WebAppConfiguration
public class ScriptRepositoryTest{

    private MockMvc mockMvc;

    @Autowired
    private ScriptRepository scriptRepositoryMock;


    @After
    public void tearDown() {
        scriptRepositoryMock.deleteAll();
    }

    @Test
    public void testSave() {
        Script script = new Script();
        script.setBody("print('task1')");
        script.setStatus(Script.Status.NEW);
        script.setResult("task1");
        script = scriptRepositoryMock.save(script);
        assertNotNull(script.getId());
        assertTrue(scriptRepositoryMock.findAll().iterator().hasNext());
    }

    @Test
    public void testFindAll() {
        Iterable results=scriptRepositoryMock.findAll();
        assertFalse(results.iterator().hasNext());
    }

    @Test
    public void testDeleteID() {
        Script script = new Script();
        script.setBody("print('task1')");
        script.setStatus(Script.Status.NEW);
        script.setResult("task1");
        script = scriptRepositoryMock.save(script);
        assertTrue(scriptRepositoryMock.findAll().iterator().hasNext());
        scriptRepositoryMock.delete(script.getId());
        assertFalse(scriptRepositoryMock.findAll().iterator().hasNext());
    }


}