package com.gmail.likhachev96.minesweeper.controllers;

import com.gmail.likhachev96.minesweeper.Configuration;
import com.gmail.likhachev96.minesweeper.models.Board;
import com.gmail.likhachev96.minesweeper.models.Cell;
import com.gmail.likhachev96.minesweeper.views.IBoardView;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class BoardController {
  private Board board;
  private ArrayList<IBoardView> views = new ArrayList<>();
  private Configuration configuration;

  public BoardController() {
    this(Configuration.EASY);
  }

  public BoardController(Configuration configuration) {
    startNewGame(configuration);
  }

  public void startNewGame() {
    startNewGame(this.configuration);
  }

  public void startNewGame(Configuration configuration) {
    setConfiguration(configuration);
    this.board = new Board(configuration.getWidth(), configuration.getHeight(), configuration.getMines());
    this.views.stream().filter(view -> view != null).forEach(view -> view.setBoard(this.board));
  }

  public void exit() {
    System.exit(0);
  }

  public void showHighScores() {
    this.views.forEach(view -> view.showHighScores(Board.getHighScores().stream().sorted().collect(Collectors.toList())));
  }

  public boolean click(int x, int y) {
    return this.board.click(x, y);
  }

  public boolean click(Cell cell) {
    return this.board.click(cell);
  }

  public void flag(int x, int y) {
    this.board.flag(x, y);
  }

  public void flag(Cell cell) {
    this.board.flag(cell);
  }

  public Cell getCell(int x, int y) {
    return this.board.getCell(x, y);
  }

  public void addView(IBoardView view) {
    this.views.add(view);
  }

  public void onViewInitialized(IBoardView view) {
    view.setBoard(this.board);
  }

  public void showAbout() {
    this.views.forEach(IBoardView::showAbout);
  }

  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
