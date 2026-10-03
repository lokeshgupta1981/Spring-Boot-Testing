package com.howtodoinjava.mockmvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * In-memory task store. The controller tests replace this bean with a Mockito mock.
 */
@Service
public class TaskService {

  private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
  private final AtomicLong ids = new AtomicLong();

  public List<Task> findAll() {
    return new ArrayList<>(tasks.values());
  }

  public Task findById(Long id) {
    Task task = tasks.get(id);
    if (task == null) {
      throw new TaskNotFoundException(id);
    }
    return task;
  }

  public Task create(Task task) {
    Task saved = new Task(ids.incrementAndGet(), task.title(), task.done());
    tasks.put(saved.id(), saved);
    return saved;
  }

  public Task update(Long id, Task task) {
    findById(id);
    Task saved = new Task(id, task.title(), task.done());
    tasks.put(id, saved);
    return saved;
  }

  public void delete(Long id) {
    if (tasks.remove(id) == null) {
      throw new TaskNotFoundException(id);
    }
  }
}
