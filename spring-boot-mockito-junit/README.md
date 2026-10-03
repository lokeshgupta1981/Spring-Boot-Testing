# Spring Boot, Mockito and JUnit Example

Source code for the article [Spring Boot, Mockito and JUnit Example](https://howtodoinjava.com/spring-boot2/testing/spring-boot-mockito-junit-example/).

Unit tests of a Spring Boot service (`SongService`) with a mocked Spring Data repository:

- `SongServiceTest`: `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `when().thenReturn()`,
  `thenThrow()`, `doThrow()`, `verify()`, `verifyNoInteractions()`, `verifyNoMoreInteractions()`, `ArgumentCaptor`, `lenient()`
- `SongServiceBddTest`: the same style of tests with `BDDMockito.given()` and `then()`
- `SongServiceSpringBootTest`: `@SpringBootTest` with `@MockitoBean` (replacement for the removed `@MockBean`)
- `StrictStubsDemoTest`: fails on purpose (`UnnecessaryStubbingException`, `PotentialStubbingProblem`, `NoInteractionsWanted`);
  excluded by the `fails-on-purpose` tag. `StrictStubsTest` runs it and checks the exceptions.
- `LenientSettingsTest`: `@MockitoSettings(strictness = Strictness.LENIENT)`

## Versions

- Java 25
- Spring Boot 4.1.1
- JUnit Jupiter 6.1.3
- Mockito 5.24.0 (loaded as a Java agent by Surefire, see `pom.xml`)

## Run

```bash
mvn test                                                      # 19 tests
mvn test -Dtest=StrictStubsDemoTest -DexcludedGroups=none     # see the strict stubs errors (3 failures)
```
