package com.gmail.likhachev96.minesweeper.models;

import com.gmail.likhachev96.minesweeper.Minesweeper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class Board {
    private final int width;
    private final int height;
    private final int minesCount;
    private boolean gameOver = false;

    private ArrayList<Cell> board;
    private boolean minesInitialized = false;

    private final long startTime;
    private long finishTime;

    public Board(int width, int height, int minesCount) {
        this.width = width;
        this.height = height;
        this.minesCount = minesCount;

        this.board = new ArrayList<>(width * height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.board.add(new Cell(this, x, y));
            }
        }

        Minesweeper.LOGGER.info("Board initialized with size {}x{} and {} mines", width, height, minesCount);
        this.startTime = System.currentTimeMillis();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameWon() {
        for (Cell s : board) {
            if (!s.isRevealed() && !s.isMine()) return false;
        }

        return true;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    private void initMines(Cell clickedCell) {
        List<Cell> possibilities = this.board.stream().filter(cell -> !cell.equals(clickedCell)).collect(Collectors.toList());

        for (int i = 0; i < minesCount; i++) {
            int index = getRandomNumber(0, possibilities.size() - 1); // We get a random in the possibilities
            Cell mine = possibilities.get(index); // We get the corresponding square
            mine.setMine(); // We set it as a mine
            possibilities.remove(index); // Then this square is out of the possibilities for next mine assignment
        }

        this.minesInitialized = true;
        Minesweeper.LOGGER.info("Mines initialized (preventing mine on first click)");
    }

    private static Random rand = new Random();
    public static int getRandomNumber(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public ArrayList<Cell> getNeighbors(Cell cell) {
        ArrayList<Cell> neighbors = new ArrayList<>();

        int x = cell.getX();
        int y = cell.getY();

        int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
        int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

        for (int i = 0; i < dx.length; i++) {
            Cell adj = this.getCell(x + dx[i], y + dy[i]);
            if (adj != null) neighbors.add(adj);
        }

        return neighbors;
    }

    public boolean click(int x, int y) {
        Cell cell = this.getCell(x, y);
        return click(cell);
    }

    public boolean click(Cell cell) {
        if (cell == null) return false;
        if (!this.minesInitialized) {
            this.initMines(cell);
        }
        if (!cell.isMarked() && !this.isGameOver()) { // Marked square are protected
            if (!cell.isRevealed()) {
                cell.setRevealed(true);

                // Win condition
                if (this.isGameWon()) {
                    this.setGameOver(true);
                    finishTime = System.currentTimeMillis();
                    saveScore();
                    Minesweeper.LOGGER.info("User has won!");
                    return true;
                }

                if (cell.isMine()) {
                    this.setGameOver(true);
                    Minesweeper.LOGGER.info("User has lost!");
                } else if (cell.countNearestMines() == 0) {
                    // If it is 0, we expend
                    ArrayList<Cell> neighbors = this.getNeighbors(cell);
                    neighbors.forEach(this::click);
                }
                return true;
            } else {
                // reveal neighbors if nb_marked around == nb_mines_around
                ArrayList<Cell> neighbors = this.getNeighbors(cell);
                int nbMarked = 0;
                for (Cell n : neighbors) {
                    if (n.isMarked()) nbMarked++;
                }
                if (nbMarked == cell.countNearestMines()) {
                    neighbors.stream().filter(n -> !n.isRevealed() && !n.isMarked()).forEach(this::click);
                }
            }
        }
        return false;
    }

    private void saveScore() {
        List<Score> scores = getHighScores();
        scores.add(new Score(System.getProperty("user.name"), getTimeToSolve()));
        Minesweeper.LOGGER.info("Time taken: {}", getTimeToSolve());
        saveHighScores(scores);
    }

    public void flag(int x, int y) {
        Cell cell = this.getCell(x, y);
        flag(cell);
    }

    public void flag(Cell cell) {
        if (cell != null) {
            if (!cell.isRevealed()) {
                cell.setMarked();
            } else {
                // mark all neighbors if nb_neighbors unrevealed == nb_mines around
                ArrayList<Cell> neighbors = this.getNeighbors(cell);
                int nbUnrevealed = 0;
                for (Cell n : neighbors) {
                    if (!n.isRevealed()) nbUnrevealed++;
                }
                if (nbUnrevealed == cell.countNearestMines()) {
                    neighbors.stream().filter(n -> !n.isRevealed() && !n.isMarked()).forEach(this::flag);
                }
            }
        }
    }

    public Cell getCell(int x, int y) {
        if (isInBoard(x, y)) {
            int index = geInternalIndex(x, y);
            return board.get(index);
        } else {
            return null;
        }
    }

    public boolean isInBoard(int x, int y) {
        return (x >= 0 && y >= 0 && x < this.width && y < this.height);
    }

    private int geInternalIndex(int x, int y) {
        return  y * this.width + x;
    }

    public long getTimeToSolve() {
        return this.isGameWon() ? this.finishTime - this.startTime : - 1;
    }

    public static List<Score> getHighScores() {
        try {
            File file = new File("minesweeper_scores.bin");
            if (!file.isFile()) {
                Minesweeper.LOGGER.info("There is no high scores list yet");
                return new ArrayList<>();
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oin = new ObjectInputStream(fis);
            ArrayList<Score> highScores = (ArrayList<Score>) oin.readObject();
            Minesweeper.LOGGER.info("High scores list loaded successfully");
            return highScores;
        } catch (Exception ex) {
            Minesweeper.LOGGER.error("Error occurred while loading high scores list");
            return new ArrayList<>();
        }
    }

    private static void saveHighScores(List<Score> scores) {
        try {
            FileOutputStream fos = new FileOutputStream("minesweeper_scores.bin");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(scores);
            oos.flush();
            oos.close();
            Minesweeper.LOGGER.info("High scores list saved successfully");
        } catch (Exception ex) {
            Minesweeper.LOGGER.error("Error occurred while saving high scores list");
        }
    }
}
