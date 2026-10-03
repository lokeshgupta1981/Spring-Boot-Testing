package com.howtodoinjava.songs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Plain unit tests of SongService: Mockito creates a mock SongRepository, no Spring context starts.
 */
@ExtendWith(MockitoExtension.class)
class SongServiceTest {

  @Mock
  SongRepository songRepository;

  @InjectMocks
  SongService songService;

  @Captor
  ArgumentCaptor<Song> songCaptor;

  @Test
  void getSongReturnsSongFromRepository() {
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    Song song = songService.getSong(1L);

    assertThat(song.getTitle()).isEqualTo("Yesterday");
    verify(songRepository).findById(1L);
  }

  @Test
  void getSongThrowsWhenRepositoryHasNoSong() {
    when(songRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> songService.getSong(99L))
        .isInstanceOf(SongNotFoundException.class)
        .hasMessage("Song 99 not found");
  }

  @Test
  void totalSecondsAddsUpSongsOfArtist() {
    when(songRepository.findByArtist("Adele")).thenReturn(List.of(
        new Song(1L, "Hello", "Adele", 295),
        new Song(2L, "Skyfall", "Adele", 286)));

    assertThat(songService.totalSeconds("Adele")).isEqualTo(581);
    assertThat(songService.totalSeconds("Nobody")).isEqualTo(0);   // unstubbed call returns an empty list
  }

  @Test
  void addSongSavesTrimmedSong() {
    when(songRepository.save(any(Song.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    songService.addSong("  Hello ", " Adele", 295);

    verify(songRepository).save(songCaptor.capture());
    Song saved = songCaptor.getValue();
    assertThat(saved.getTitle()).isEqualTo("Hello");
    assertThat(saved.getArtist()).isEqualTo("Adele");
    assertThat(saved.getSeconds()).isEqualTo(295);
  }

  @Test
  void addSongRejectsZeroSecondsWithoutTouchingRepository() {
    assertThatThrownBy(() -> songService.addSong("Hello", "Adele", 0))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("seconds must be positive");

    verifyNoInteractions(songRepository);
  }

  @Test
  void addSongPassesRepositoryExceptionToCaller() {
    when(songRepository.save(any(Song.class)))
        .thenThrow(new DataIntegrityViolationException("duplicate song"));

    assertThatThrownBy(() -> songService.addSong("Hello", "Adele", 295))
        .isInstanceOf(DataIntegrityViolationException.class)
        .hasMessage("duplicate song");
  }

  @Test
  void removeSongPassesExceptionOfVoidMethod() {
    when(songRepository.existsById(7L)).thenReturn(true);
    doThrow(new DataAccessResourceFailureException("database down"))
        .when(songRepository).deleteById(7L);

    assertThatThrownBy(() -> songService.removeSong(7L))
        .isInstanceOf(DataAccessResourceFailureException.class)
        .hasMessage("database down");
  }

  @Test
  void removeSongChecksThenDeletes() {
    when(songRepository.existsById(7L)).thenReturn(true);

    songService.removeSong(7L);

    verify(songRepository).existsById(7L);
    verify(songRepository, times(1)).deleteById(7L);
    verifyNoMoreInteractions(songRepository);
  }

  @Test
  void removeSongNeverDeletesUnknownSong() {
    when(songRepository.existsById(8L)).thenReturn(false);

    assertThatThrownBy(() -> songService.removeSong(8L))
        .isInstanceOf(SongNotFoundException.class);

    verify(songRepository, never()).deleteById(any());
  }

  @Test
  void lenientStubMayStayUnused() {
    lenient().when(songRepository.existsById(1L)).thenReturn(true);   // unused, no error
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    assertThat(songService.getSong(1L).getTitle()).isEqualTo("Yesterday");
  }
}
