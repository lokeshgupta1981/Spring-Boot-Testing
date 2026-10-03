package com.howtodoinjava.mockmvc;

public class TaskNotFoundException extends RuntimeException {

  public TaskNotFoundException(Long id) {
    super("Task " + id + " not found");
  }
}
