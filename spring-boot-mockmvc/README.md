# Spring Boot MockMvc Example

Source code for the article [Spring Boot MockMvc Example with @WebMvcTest](https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockmvc-example/).

A small to-do list REST API (`/tasks`) and its controller tests:

- `TaskControllerTest`: `@WebMvcTest` + `MockMvc` + `@MockitoBean` for GET, POST, PUT, DELETE, validation errors (400), wrong content type (415) and `@RestControllerAdvice` (404).
- `TaskControllerTesterTest`: the same checks with `MockMvcTester` (AssertJ style).
- `TaskControllerWithoutAdviceTest`: behavior without an exception handler (unhandled exception, empty 400 body, missing primitive field).
- `TaskApiIntegrationTest`: `@SpringBootTest` + `@AutoConfigureMockMvc` with the real service.

## Versions

- Java 25
- Spring Boot 4.1.1 (Spring Framework 7.0.9, JUnit Jupiter 6.0.3, Mockito 5.23.0, Jackson 3.1.5)

## Run

```bash
mvn test
```

Expected result: `Tests run: 20, Failures: 0, Errors: 0, Skipped: 0`.
