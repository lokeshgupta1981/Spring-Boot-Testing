package com.howtodoinjava.songs;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SongService {

  private final SongRepository songRepository;

  public SongService(SongRepository songRepository) {
    this.songRepository = songRepository;
  }

  public Song getSong(Long id) {
    return songRepository.findById(id)
        .orElseThrow(() -> new SongNotFoundException(id));
  }

  public int totalSeconds(String artist) {
    return songRepository.findByArtist(artist).stream()
        .mapToInt(Song::getSeconds)
        .sum();
  }

  public Song addSong(String title, String artist, int seconds) {
    if (seconds <= 0) {
      throw new IllegalArgumentException("seconds must be positive");
    }
    return songRepository.save(new Song(title.trim(), artist.trim(), seconds));
  }

  public void removeSong(Long id) {
    if (!songRepository.existsById(id)) {
      throw new SongNotFoundException(id);
    }
    songRepository.deleteById(id);
  }
}
