package com.grinyov.service;

import com.grinyov.ScriptengineApiApplication;
import com.grinyov.dao.ScriptRepository;
import com.grinyov.model.Script;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by vgrinyov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptengineApiApplication.class)
@WebAppConfiguration
public class ScriptProccessingServiceTest {

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    @Autowired
    private ScriptRepository scriptRepositoryMock;

    @Before
    public void init() throws Exception {
        List<String> scripts = new ArrayList<>();
        scripts.add("print('Script executed!');");
        scripts.add("while (true) {\n" +
                "  // ...\n" +
                "}");
        scripts.add("alert( typeof window.methodThatDoesntExist );");

        scripts.forEach(item -> {
            Script script = new Script();
            script.setBody(item);
            script.setStatus(Script.Status.NEW);
            script.setResult("Script result: ");
            scriptRepositoryMock.save(script);
        });

    }

    @Test
    public void performNormal() throws Exception {
        scriptProccessingService.perform(1L);
        Thread.sleep(200);
        assertEquals(Script.Status.RUNNING, scriptRepositoryMock.findOne(1L).getStatus());
    }

    @Test
    public void performLoopedScript() throws Exception {
        scriptProccessingService.perform(2L);
        Thread.sleep(3000);
        assertEquals(Script.Status.RUNNING, scriptRepositoryMock.findOne(2L).getStatus());
    }

    @Test
    public void detail() throws Exception {
        assertNotNull(scriptRepositoryMock.findOne(1L).getResult());
    }

}