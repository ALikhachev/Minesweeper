package com.gmail.likhachev96.minesweeper.views;

import com.gmail.likhachev96.minesweeper.controllers.BoardController;
import com.gmail.likhachev96.minesweeper.models.Board;
import com.gmail.likhachev96.minesweeper.models.Score;

import java.util.List;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public interface IBoardView {

    void setBoard(Board board);

    void redraw();

    BoardController getController();

    void showAbout();

    void showHighScores(List<Score> scores);

}
