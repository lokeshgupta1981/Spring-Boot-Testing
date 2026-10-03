package com.howtodoinjava.songs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * The same kind of tests written in the given/when/then style of BDDMockito.
 */
@ExtendWith(MockitoExtension.class)
class SongServiceBddTest {

  @Mock
  SongRepository songRepository;

  @InjectMocks
  SongService songService;

  @Test
  void totalSecondsOfArtist() {
    // given
    given(songRepository.findByArtist("Adele")).willReturn(List.of(
        new Song(1L, "Hello", "Adele", 295),
        new Song(2L, "Skyfall", "Adele", 286)));

    // when
    int total = songService.totalSeconds("Adele");

    // then
    assertThat(total).isEqualTo(581);
    then(songRepository).should().findByArtist("Adele");
    then(songRepository).shouldHaveNoMoreInteractions();
  }

  @Test
  void removeSongWhenDatabaseIsDown() {
    given(songRepository.existsById(7L)).willReturn(true);
    willThrow(new DataAccessResourceFailureException("database down"))
        .given(songRepository).deleteById(7L);

    assertThatThrownBy(() -> songService.removeSong(7L))
        .isInstanceOf(DataAccessResourceFailureException.class)
        .hasMessage("database down");
  }

  @Test
  void addSongWithNegativeSecondsSavesNothing() {
    assertThatThrownBy(() -> songService.addSong("Hello", "Adele", -1))
        .isInstanceOf(IllegalArgumentException.class);

    then(songRepository).should(never()).save(any());
  }
}
