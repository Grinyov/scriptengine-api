package com.grinyov.controller;

import com.grinyov.model.Script;
import com.grinyov.service.ScriptProccessingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by vgrinyov
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ScriptResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScriptProccessingService scriptProccessingService;


    @Test
    public void perform() throws Exception {

        Script script = script();
        String requestBody = saveRequestJsonString(script);


          ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .put("/scripts/{id}/running")
                .accept(MediaTypes.HAL_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        final Script runningScript = findRunningScript();
        resultActions.andExpect(status().isAccepted())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/scripts/{id}/running" ))
                .andExpect(jsonPath("$.id", is(script.getId())))
                .andExpect(jsonPath("$.script", is(script.getScript())))
                .andExpect(jsonPath("$.currency", is(script.getStatus().equals(Script.Status.NEW))))
                .andExpect(jsonPath("$.comment", is(script.getResult())));

    }

    @Test
    public void viewOne() throws Exception {
        //PersistentEntityResource entityResource = src.viewOne(1L, null);
    }

    @Test
    public void terminateOne() throws Exception {
        //PersistentEntityResource entityResource = src.terminateOne(1L, null);
    }


      private Script script() {
          Script script = new Script();
          script.setId(1l);
          script.setScript("while(true)");
          script.setStatus(Script.Status.NEW);
          script.setResult("task1: ");
          return script;
      }

    private static String saveRequestJsonString(Script script) {
        return "{\n" +
                "  \"id\": \"" + script.getId() + "\",\n" +
                "  \"script\": " + script.getScript() + ",\n" +
                "  \"status\": \"" + script.getStatus() + "\",\n" +
                "  \"result\": \"" + script.getResult() + "\"\n" +
                "}";
    }

    private Script findRunningScript(){return null;}

}


