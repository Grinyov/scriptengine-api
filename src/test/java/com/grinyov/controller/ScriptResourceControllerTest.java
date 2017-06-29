package com.grinyov.controller;

import com.grinyov.dao.ScriptRepository;
import com.grinyov.model.Script;
import com.grinyov.service.ScriptProccessingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by vgrinyov
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ScriptResourceController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
public class ScriptResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScriptProccessingService scriptProccessingService;

    @MockBean
    private ScriptRepository scriptRepository;


    @Test
    public void setScriptById() throws Exception {
        Script script = new Script();
        script.setScript("print('task1')");
        given(this.scriptRepository.findOne(1L)).willReturn(script);

        mockMvc.perform(get("/script/" + 1L))
                .andExpect(status().isOk());
    }


    @Test
    public void perform() throws Exception {



        Script script = new Script();
        script.setScript("print('task1')");
        given(this.scriptProccessingService.perform(1L)).willReturn(script);
        mockMvc.perform(put("/scripts/" + 1L + "/running")
                .content("print('task1')"))
                .andExpect(status().isOk());



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
//          script.setId(1L);
          script.setScript("while(true)");
          script.setStatus(Script.Status.NEW);
          script.setResult("task1: ");
          return script;
      }

    private static String saveRequestJsonString(Script script) {
        return "{\n" +
//                "  \"id\": \"" + script.getId() + "\",\n" +
                "  \"script\": " + script.getScript() + ",\n" +
                "  \"status\": \"" + script.getStatus() + "\",\n" +
                "  \"result\": \"" + script.getResult() + "\"\n" +
                "}";
    }

    private Script findRunningScript(){
        return null;
    }

}


