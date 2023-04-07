package com.project.apidbtester.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.apidbtester.testapis.dtos.TestResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testPostRequest_WhenPassed() throws Exception {

        String apibody = "{\n" +
                "    \"testCaseDetails\": {\n" +
                "        \"type\": \"post\",\n" +
                "        \"url\": \"http://localhost:9191/addProducer\",\n" +
                "        \"payload\": \"{\\\"first_name\\\":\\\"Rohit\\\",\\\"last_name\\\":\\\"Sharma\\\",\\\"gender\\\":\\\"m\\\",\\\"film_count\\\":73}\",\n" +
                "        \"tableName\": \"producers\"\n" +
                "    },\n" +
                "    \"columnValues\": [\n" +
                "        {\n" +
                "            \"columnName\": \"first_name\",\n" +
                "            \"expectedValue\": \"Rohit\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"columnName\": \"last_name\",\n" +
                "            \"expectedValue\": \"Sharma\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(apibody))
                .andExpect(status().isOk())
                .andReturn();

        TestResponse testResponse = objectMapper.readValue(result.getResponse().getContentAsString(), TestResponse.class);
        assertEquals(Optional.ofNullable(testResponse.getHttpStatusCode()), Optional.of(200));
        assertEquals(testResponse.getAllTestPassed(), true);

    }

    @Test
    public void testPostRequest_WhenFailed() throws Exception {

        String s = "{\n" +
                "    \"testCaseDetails\": {\n" +
                "        \"type\": \"post\",\n" +
                "        \"url\": \"http://localhost:9191/addProducer\",\n" +
                "        \"payload\": \"{\\\"first_name\\\":\\\"Rohit\\\",\\\"last_name\\\":\\\"Sharma\\\",\\\"gender\\\":\\\"m\\\",\\\"film_count\\\":73}\",\n" +
                "        \"tableName\": \"producers\"\n" +
                "    },\n" +
                "    \"columnValues\": [\n" +
                "        {\n" +
                "            \"columnName\": \"first_name\",\n" +
                "            \"expectedValue\": \"Rohit\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"columnName\": \"last_name\",\n" +
                "            \"expectedValue\": \"Kohli\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpect(status().isOk())
                .andReturn();

        TestResponse testResponse = objectMapper.readValue(result.getResponse().getContentAsString(), TestResponse.class);
        assertEquals(Optional.ofNullable(testResponse.getHttpStatusCode()), Optional.of(200));
        assertEquals(testResponse.getAllTestPassed(), false);

    }
}
