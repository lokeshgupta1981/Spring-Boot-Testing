package com.howtodoinjava.mockmvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Full application context with the real TaskService, still without a running server.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TaskApiIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void createThenReadTask() throws Exception {
    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "Water plants", "done": false}
                """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));

    mockMvc.perform(get("/tasks/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Water plants"));
  }
}
