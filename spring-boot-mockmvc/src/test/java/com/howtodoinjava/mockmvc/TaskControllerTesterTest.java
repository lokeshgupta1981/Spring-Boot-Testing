package com.howtodoinjava.mockmvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

/**
 * The same tests written with MockMvcTester (AssertJ style, Spring Framework 6.2+).
 */
@WebMvcTest(TaskController.class)
class TaskControllerTesterTest {

  @Autowired
  MockMvcTester mvc;

  @MockitoBean
  TaskService taskService;

  @Test
  void getTaskById() {
    given(taskService.findById(1L)).willReturn(new Task(1L, "Buy milk", false));

    assertThat(mvc.get().uri("/tasks/{id}", 1))
        .hasStatusOk()
        .bodyJson()
        .extractingPath("$.title").isEqualTo("Buy milk");
  }

  @Test
  void getTaskByIdAsObject() {
    given(taskService.findById(1L)).willReturn(new Task(1L, "Buy milk", false));

    assertThat(mvc.get().uri("/tasks/{id}", 1))
        .hasStatusOk()
        .bodyJson()
        .convertTo(Task.class)
        .isEqualTo(new Task(1L, "Buy milk", false));
  }

  @Test
  void createTask() {
    given(taskService.create(any(Task.class))).willReturn(new Task(3L, "Call mom", false));

    assertThat(mvc.post().uri("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "Call mom", "done": false}
                """))
        .apply(print())
        .hasStatus(HttpStatus.CREATED)
        .hasHeader("Location", "/tasks/3");
  }

  @Test
  void createTaskWithBlankTitle() {
    assertThat(mvc.post().uri("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "", "done": false}
                """))
        .hasStatus(HttpStatus.BAD_REQUEST)
        .bodyJson()
        .extractingPath("$.errors.title").isEqualTo("must not be blank");
  }

  @Test
  void missingTask() {
    given(taskService.findById(99L)).willThrow(new TaskNotFoundException(99L));

    assertThat(mvc.get().uri("/tasks/{id}", 99))
        .hasStatus(HttpStatus.NOT_FOUND)
        .bodyJson()
        .extractingPath("$.detail").isEqualTo("Task 99 not found");
  }
}
