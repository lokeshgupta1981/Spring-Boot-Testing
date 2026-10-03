package com.howtodoinjava.mockmvc;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> all() {
    return taskService.findAll();
  }

  @GetMapping("/{id}")
  public Task one(@PathVariable Long id) {
    return taskService.findById(id);
  }

  @PostMapping
  public ResponseEntity<Task> create(@Valid @RequestBody Task task) {
    Task saved = taskService.create(task);
    return ResponseEntity.created(URI.create("/tasks/" + saved.id())).body(saved);
  }

  @PutMapping("/{id}")
  public Task update(@PathVariable Long id, @Valid @RequestBody Task task) {
    return taskService.update(id, task);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    taskService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
