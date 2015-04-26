package com.gmail.likhachev96.minesweeper.models;

import java.io.Serializable;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class Score implements Serializable, Comparable {
  private final String username;
  private final long time;
  private final static long serialVersionUID = -3823811991813715970L;

  public Score(String username, long time) {
    this.username = username;
    this.time = time;
  }

  public String getUsername() {
    return username;
  }

  public long getTimeInSeconds() {
    return time / 1000L;
  }

  @Override
  public int compareTo(Object o) {
    if (o instanceof Score) {
      return this.time > ((Score) o).time ? 1 : -1;
    }
    return 0;
  }
}
