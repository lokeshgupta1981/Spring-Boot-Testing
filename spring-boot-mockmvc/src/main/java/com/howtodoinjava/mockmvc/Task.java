package com.howtodoinjava.mockmvc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * A to-do task. The id is assigned by the server.
 */
public record Task(Long id,
                   @NotBlank @Size(max = 40) String title,
                   boolean done) {
}
