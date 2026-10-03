package com.howtodoinjava.songs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;

import org.junit.jupiter.api.Test;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.platform.testkit.engine.Events;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.exceptions.misusing.UnnecessaryStubbingException;
import org.mockito.exceptions.verification.NoInteractionsWanted;

/**
 * Runs the tests of StrictStubsDemoTest and checks that strict stubs make them fail.
 * No MockitoExtension here: the demo tests open their own Mockito session.
 */
class StrictStubsTest {

  @Test
  void unusedStubFailsWithUnnecessaryStubbingException() {
    assertThat(failureOf("unusedStub")).isInstanceOf(UnnecessaryStubbingException.class);
  }

  @Test
  void stubWithOtherArgumentFailsWithPotentialStubbingProblem() {
    assertThat(failureOf("stubWithOtherArgument")).isInstanceOf(PotentialStubbingProblem.class);
  }

  @Test
  void unverifiedCallFailsVerifyNoMoreInteractions() {
    assertThat(failureOf("deleteNotVerified")).isInstanceOf(NoInteractionsWanted.class);
  }

  private static Throwable failureOf(String method) {
    Events tests = EngineTestKit.engine("junit-jupiter")
        .selectors(selectMethod(StrictStubsDemoTest.class, method))
        .execute()
        .testEvents();
    assertThat(tests.failed().count()).isEqualTo(1);
    return tests.failed().list().getFirst()
        .getPayload(TestExecutionResult.class).orElseThrow()
        .getThrowable().orElseThrow();
  }
}
