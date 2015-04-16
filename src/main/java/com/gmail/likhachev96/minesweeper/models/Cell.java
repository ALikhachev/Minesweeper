package com.gmail.likhachev96.minesweeper.models;

import java.util.ArrayList;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class Cell {
    private final int x;
    private final int y;
    private final Board board;

    private boolean marked;
    private boolean mine;
    private boolean revealed;
    private int numberOfNearestMines = 0;

    public Cell(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked() {
        this.marked = !this.marked;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine() {
        this.mine = true;
        ArrayList<Cell> adjacents = this.board.getNeighbors(this);
        adjacents.forEach(com.gmail.likhachev96.minesweeper.models.Cell::addMineAround);
    }

    private void addMineAround() {
        ++numberOfNearestMines;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public int countNearestMines() {
        return this.numberOfNearestMines;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
