package com.howtodoinjava.songs;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Song {

  @Id
  @GeneratedValue
  private Long id;
  private String title;
  private String artist;
  private int seconds;

  protected Song() {
  }

  public Song(String title, String artist, int seconds) {
    this.title = title;
    this.artist = artist;
    this.seconds = seconds;
  }

  public Song(Long id, String title, String artist, int seconds) {
    this(title, artist, seconds);
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getArtist() {
    return artist;
  }

  public int getSeconds() {
    return seconds;
  }

  @Override
  public String toString() {
    return "Song[id=" + id + ", title=" + title + ", artist=" + artist + ", seconds=" + seconds + "]";
  }
}
