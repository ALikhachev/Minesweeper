package com.gmail.likhachev96.minesweeper.models;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
@SuppressWarnings("unused")
public class CellTest {
  @Test
  public void initTest() {
    Cell cell = new Cell(null, 1, 2);
    assertFalse(cell.isMarked());
    assertFalse(cell.isMine());
    assertFalse(cell.isRevealed());
    assertThat(cell.getX(), is(1));
    assertThat(cell.getY(), is(2));
  }

  @Test
  public void markTest() {
    Cell cell = new Cell(null, 1, 2);
    cell.setMarked();
    assertTrue(cell.isMarked());
    assertFalse(cell.isMine());
    assertFalse(cell.isRevealed());
  }

  @Test
  public void revealTest() {
    Cell cell = new Cell(null, 1, 2);
    cell.setRevealed();
    assertFalse(cell.isMarked());
    assertFalse(cell.isMine());
    assertTrue(cell.isRevealed());
  }

  @Test
  public void mineTest() {
    Board board = new Board(1, 1, 1);
    Cell cell = new Cell(board, 1, 2);
    cell.setMine();
    assertFalse(cell.isMarked());
    assertTrue(cell.isMine());
    assertFalse(cell.isRevealed());
  }

  @Test
  public void nearestMinesTest() {
    Board board = new Board(10, 10, 10);
    Field f = null;
    try {
      f = Cell.class.getDeclaredField("mine");
    } catch (NoSuchFieldException e) {
      // we have this field
    }
    f.setAccessible(true);

    for (int i = 0; i < board.getWidth(); ++i) {
      for (int j = 0; j < board.getHeight(); ++j) {
        try {
          f.setBoolean(board.getCell(i, j), false);
        } catch (IllegalAccessException e) {
          // we exactly have access
        }
      }
    }
    Cell c1 = board.getCell(0, 0);
    Cell c2 = board.getCell(0, 1);
    c1.setMine();
    assertThat(c1.countNearestMines(), is(0));
    assertThat(c2.countNearestMines(), is(1));
    c2.setMine();
    assertThat(c1.countNearestMines(), is(1));
    assertThat(c2.countNearestMines(), is(1));
  }
}
