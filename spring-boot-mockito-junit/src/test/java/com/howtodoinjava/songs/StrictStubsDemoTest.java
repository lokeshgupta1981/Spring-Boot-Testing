package com.howtodoinjava.songs;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * All tests fail on purpose to show the errors of strict stubs and verifyNoMoreInteractions().
 * Excluded from the normal build by the "fails-on-purpose" tag; StrictStubsTest runs them and checks the errors.
 */
@Tag("fails-on-purpose")
@ExtendWith(MockitoExtension.class)
class StrictStubsDemoTest {

  @Mock
  SongRepository songRepository;

  @InjectMocks
  SongService songService;

  @Test
  void unusedStub() {
    when(songRepository.existsById(1L)).thenReturn(true);   // never used by getSong()
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    songService.getSong(1L);
  }

  @Test
  void stubWithOtherArgument() {
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    songService.getSong(2L);   // the code calls findById(2L)
  }

  @Test
  void deleteNotVerified() {
    when(songRepository.existsById(7L)).thenReturn(true);

    songService.removeSong(7L);

    verifyNoMoreInteractions(songRepository);   // deleteById(7L) was not verified
  }
}
