package com.howtodoinjava.mockmvc;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * What happens when no @RestControllerAdvice handles the exception.
 * ApiExceptionHandler is excluded from this slice on purpose.
 */
@WebMvcTest(controllers = TaskController.class,
    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ApiExceptionHandler.class))
class TaskControllerWithoutAdviceTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  TaskService taskService;

  @Test
  void unhandledExceptionReachesTheTest() {
    given(taskService.findById(99L)).willThrow(new TaskNotFoundException(99L));

    assertThatThrownBy(() -> mockMvc.perform(get("/tasks/{id}", 99)))
        .hasCauseInstanceOf(TaskNotFoundException.class);
  }

  @Test
  void defaultValidationErrorHasEmptyBody() throws Exception {
    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "", "done": false}
                """))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(""));
  }

  @Test
  void missingPrimitiveFieldReturns400() throws Exception {
    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {"title": "Call mom"}
                """))
        .andExpect(status().isBadRequest());
  }
}
