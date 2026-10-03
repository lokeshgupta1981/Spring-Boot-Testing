package com.howtodoinjava.songs;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * Turns strict stubs off for the whole class. Prefer lenient() on single stubs.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LenientSettingsTest {

  @Mock
  SongRepository songRepository;

  @InjectMocks
  SongService songService;

  @Test
  void unusedStubAllowedForWholeClass() {
    when(songRepository.existsById(1L)).thenReturn(true);   // unused, no error
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    // No PotentialStubbingProblem: findById(2L) returns the default Optional.empty()
    assertThatThrownBy(() -> songService.getSong(2L))
        .isInstanceOf(SongNotFoundException.class)
        .hasMessage("Song 2 not found");
  }
}
