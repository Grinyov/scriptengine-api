package com.grinyov.controller;

import com.grinyov.ScriptengineApiApplication;
import com.grinyov.dao.ScriptRepository;
import com.grinyov.model.Script;
import com.grinyov.service.ScriptProccessingService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by vgrinyov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScriptengineApiApplication.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs("build/generated-snippets")
public class ScriptResourceControllerTest {

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("docs/api-guide.html");

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Autowired
    private ScriptProccessingService scriptProccessingService;

    @Autowired
    private ScriptRepository scriptRepository;

    @Test
    public void getMainPage() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(content().string(containsString("_links\"")));
    }

    @Test
    public void createScript() throws Exception {
        Script script =  script();
        String requestBody = saveRequestJsonString(script);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .post("/scripts")
                .accept(MediaTypes.HAL_JSON)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON));

        final Script createdScript = findCreatedScript();
        resultActions.andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost:8080/scripts/" + createdScript.getId()))
                .andExpect(jsonPath("$.id", is(createdScript.getId().intValue())))
                .andExpect(jsonPath("$.script", is(createdScript.getScript())))
                .andExpect(jsonPath("$.status", is(createdScript.getStatus())))
                .andExpect(jsonPath("$.result", is(createdScript.getResult())));


        resultActions.andDo(document("create-script",
                links(halLinks(),
                        linkWithRel("curies").description("CUR-ies"),
                        linkWithRel("self").description("This script"),
                        linkWithRel("api:script").description("This <<scripts, script>>"),
                        linkWithRel("api:perform").description("Running the script via PUT"),
                        linkWithRel("api:view").description("View detail the script via PUT"),
                        linkWithRel("api:terminate").description("Termination of the script via PUT with empty body")
                ),
                responseFields(
                        fieldWithPath("_links").type(JsonFieldType.OBJECT).description("Links"),
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Unique script id"),
                        fieldWithPath("script").type(JsonFieldType.STRING).description("the body of the script"),
                        fieldWithPath("status").type(JsonFieldType.STRING).description("Formal script status, one of " +
                                Stream.of(Script.Status.values()).map(Enum::name).collect(Collectors.joining(", "))),
                        fieldWithPath("result").type(JsonFieldType.STRING).description("Result of running of the script")

                )));
    }

//    @Test
//    public void setScriptById() throws Exception {
//        Script script = new Script();
//        script.setId(1L);
//        script.setScript("print('task1')");
//        given(this.scriptRepository.findOne(1L)).willReturn(script);
//
//        mockMvc.perform(get("/script/" + 1L))
//                .andExpect(status().isOk());
//    }

    @Test
    public void perform() throws Exception {

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
        script.setId(1L);
        script.setScript("print('Hello from task1')");
        script.setStatus(Script.Status.NEW);
        script.setResult("task1: Some result");
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

    private Script findCreatedScript() {
        return scriptRepository.findOne(1L);//findAll(new Sort(Sort.Direction.DESC, "id")).iterator().next();
    }

}


