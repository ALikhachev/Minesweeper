package com.gmail.likhachev96.minesweeper;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public enum Configuration {
    EASY(9, 9, 10),
    MEDIUM(18, 18, 40),
    HARD(30, 16, 99),
    ;
    private final int width;
    private final int height;
    private final int mines;

    Configuration(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.mines = mines;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMines() {
        return mines;
    }
}
