package com.howtodoinjava.songs;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {

  List<Song> findByArtist(String artist);
}
