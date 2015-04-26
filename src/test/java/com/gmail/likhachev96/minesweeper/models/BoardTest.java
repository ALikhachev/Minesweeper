package com.gmail.likhachev96.minesweeper.models;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * We don't need to test {@code BoardController} as far as BoardController delegates all calls to {@code Board}
 * Copyright (c) 2015 Alexander Likhachev.
 */
@SuppressWarnings("unused")
public class BoardTest {
  @Test
  public void fillInTest() {
    Board board = new Board(10, 11, 7);
    Method m = null;
    Field f = null;
    try {
      m = Board.class.getDeclaredMethod("initMines", Cell.class);
      f = Board.class.getDeclaredField("minesInitialized");
    } catch (NoSuchMethodException | NoSuchFieldException e) {
      // we have this method
    }
    m.setAccessible(true);
    f.setAccessible(true);
    boolean minesReady;
    try {
      minesReady = f.getBoolean(board);
      assertFalse(minesReady);
      m.invoke(board, board.getCell(0, 0));
      minesReady = f.getBoolean(board);
      assertTrue(minesReady);
    } catch (IllegalAccessException e) {
      // we exactly have access
    } catch (InvocationTargetException e) {
      // we exactly have invocation target
    }
    assertFalse(board.getCell(0, 0).isMine());
    int mines = 0;
    for (int i = 0; i < board.getWidth(); ++i) {
      for (int j = 0; j < board.getHeight(); ++j) {
        if (board.getCell(i, j).isMine()) {
          ++mines;
        }
        assertFalse(board.getCell(i, j).isMarked());
        assertFalse(board.getCell(i, j).isRevealed());
      }
    }
    assertThat(mines, is(7));
    assertThat(board.getMinesCount(), is(7));
    assertThat(board.getWidth(), is(10));
    assertThat(board.getHeight(), is(11));
  }

  @Test
  public void boundsTest() {
    Board board = new Board(10, 10, 7);
    assertTrue(board.isInBoard(9, 9));
    assertFalse(board.isInBoard(9, 10));
    assertFalse(board.isInBoard(9, 10));
    assertFalse(board.isInBoard(-1, 5));
    assertFalse(board.isInBoard(5, -1));
    assertTrue(board.isInBoard(0, 0));
  }

  @Test
  public void clickTest() {
    Board board = new Board(10, 10, 7);
    board.click(3, 2);
    assertFalse(board.getCell(3, 2).isMine());
    assertTrue(board.getCell(3, 2).isRevealed());
    for (int i = 0; i < board.getWidth(); ++i) {
      for (int j = 0; j < board.getHeight(); ++j) {
        assertFalse(board.getCell(i, j).isMarked());
      }
    }
  }

  @Test
  public void flagTest() {
    Board board = new Board(10, 10, 7);
    board.click(3, 2);
    board.flag(9, 9);
    assertFalse(board.getCell(3, 2).isMine());
    assertTrue(board.getCell(3, 2).isRevealed());
    for (int i = 0; i < board.getWidth(); ++i) {
      for (int j = 0; j < board.getHeight(); ++j) {
        if (i == 9 && j == 9) {
          assertTrue(board.getCell(i, j).isMarked());
        } else {
          assertFalse(board.getCell(i, j).isMarked());
        }
      }
    }
  }

  @Test
  public void wonLoseTest() {
    Board board = new Board(10, 10, 98);
    board.click(3, 2);
    board.click(3, 3);
    assertTrue(board.getCell(3, 3).isMine() && board.isGameOver() || !board.getCell(3, 3).isMine() && board.isGameWon());
  }

  private ArrayList<String> handled;

  @Test
  public void revealNeighborsTest() {
    Board board = new Board(10, 10, 7);
    board.click(3, 2);
    handled = new ArrayList<>();
    assertTrue(revealNeighborsTest0(board, board.getCell(3, 2)));
    assertTrue(revealNeighborsTest1(board, board.getCell(3, 2)));
  }

  private boolean revealNeighborsTest1(Board b, Cell c) {
    for (int i = 0; i < b.getWidth(); ++i) {
      for (int j = 0; j < b.getHeight(); ++j) {
        if (handled.contains(String.format("%d%d", i, j))) {
          continue;
        }
        return !b.getCell(i, j).isRevealed();
      }
    }
    return true;
  }


  private boolean revealNeighborsTest0(Board b, Cell c) {
    if (c == null) {
      return true;
    }
    if (handled.contains(String.format("%d%d", c.getX(), c.getY()))) {
      return true;
    }
    handled.add(String.format("%d%d", c.getX(), c.getY()));
    boolean v = c.isMine() && !c.isRevealed() || !c.isMine() && c.isRevealed();
    if (c.countNearestMines() == 0) {
      for (Cell n : b.getNeighbors(c)) {
        v &= revealNeighborsTest0(b, n);
      }
    }
    return v;
  }
}
