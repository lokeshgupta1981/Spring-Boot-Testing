package com.howtodoinjava.mockmvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

/**
 * Web-layer slice test: only TaskController, ApiExceptionHandler and the MVC infrastructure are loaded.
 * TaskService is replaced by a Mockito mock.
 */
@WebMvcTest(TaskController.class)
class TaskControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  JsonMapper jsonMapper;

  @MockitoBean
  TaskService taskService;

  @Test
  void getTaskById() throws Exception {
    given(taskService.findById(1L)).willReturn(new Task(1L, "Buy milk", false));

    mockMvc.perform(get("/tasks/{id}", 1))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.title").value("Buy milk"))
        .andExpect(jsonPath("$.done").value(false));
  }

  @Test
  void getAllTasks() throws Exception {
    given(taskService.findAll()).willReturn(List.of(
        new Task(1L, "Buy milk", false),
        new Task(2L, "Pay rent", true)));

    mockMvc.perform(get("/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].title").value("Buy milk"))
        .andExpect(jsonPath("$[1].done").value(true));
  }

  @Test
  void createTask() throws Exception {
    given(taskService.create(any(Task.class))).willReturn(new Task(3L, "Call mom", false));

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "Call mom", "done": false}
                """))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/tasks/3"))
        .andExpect(jsonPath("$.id").value(3));
  }

  @Test
  void createTaskFromObject() throws Exception {
    given(taskService.create(any(Task.class))).willReturn(new Task(3L, "Call mom", false));
    String body = jsonMapper.writeValueAsString(new Task(null, "Call mom", false));
    assertThat(body).isEqualTo("{\"id\":null,\"title\":\"Call mom\",\"done\":false}");

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(status().isCreated());

    verify(taskService).create(new Task(null, "Call mom", false));
  }

  @Test
  void updateTask() throws Exception {
    given(taskService.update(eq(1L), any(Task.class))).willReturn(new Task(1L, "Buy milk", true));

    mockMvc.perform(put("/tasks/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "Buy milk", "done": true}
                """))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.done").value(true));
  }

  @Test
  void deleteTask() throws Exception {
    mockMvc.perform(delete("/tasks/{id}", 1))
        .andExpect(status().isNoContent());

    verify(taskService).delete(1L);
  }

  @Test
  void createTaskWithBlankTitleReturns400() throws Exception {
    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "", "done": false}
                """))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.detail").value("Validation failed"))
        .andExpect(jsonPath("$.errors.title").value("must not be blank"));

    verify(taskService, never()).create(any());
  }

  @Test
  void malformedJsonReturns400() throws Exception {
    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"title\": "))
        .andExpect(status().isBadRequest());
  }

  @Test
  void wrongContentTypeReturns415() throws Exception {
    mockMvc.perform(post("/tasks")
            .contentType(MediaType.TEXT_PLAIN)
            .content("Call mom"))
        .andExpect(status().isUnsupportedMediaType());
  }

  @Test
  void missingTaskReturns404() throws Exception {
    given(taskService.findById(99L)).willThrow(new TaskNotFoundException(99L));

    mockMvc.perform(get("/tasks/{id}", 99))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
        .andExpect(jsonPath("$.title").value("Not Found"))
        .andExpect(jsonPath("$.detail").value("Task 99 not found"));
  }

  @Test
  void deleteMissingTaskReturns404() throws Exception {
    willThrow(new TaskNotFoundException(99L)).given(taskService).delete(99L);

    mockMvc.perform(delete("/tasks/{id}", 99))
        .andExpect(status().isNotFound());
  }
}
