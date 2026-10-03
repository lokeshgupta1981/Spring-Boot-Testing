package com.howtodoinjava.songs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Starts the Spring application context and replaces the SongRepository bean with a Mockito mock.
 */
@SpringBootTest
class SongServiceSpringBootTest {

  @MockitoBean
  SongRepository songRepository;

  @Autowired
  SongService songService;

  @Test
  void serviceBeanUsesMockRepository() {
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    Song song = songService.getSong(1L);

    assertThat(song.getTitle()).isEqualTo("Yesterday");
    verify(songRepository).findById(1L);
    assertThat(mockingDetails(songRepository).isMock()).isTrue();
    System.out.println("Injected repository: " + songRepository.getClass().getSimpleName());
  }

  @Test
  void unusedStubDoesNotFailInSpringContext() {
    when(songRepository.existsById(1L)).thenReturn(true);   // unused, no UnnecessaryStubbingException
    when(songRepository.findById(1L))
        .thenReturn(Optional.of(new Song(1L, "Yesterday", "The Beatles", 125)));

    assertThat(songService.getSong(1L).getArtist()).isEqualTo("The Beatles");
  }
}
