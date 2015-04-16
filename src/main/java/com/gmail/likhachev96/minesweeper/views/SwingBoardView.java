package com.gmail.likhachev96.minesweeper.views;

import assets.cells.CellsAssets;
import assets.ui.UIAssets;
import com.gmail.likhachev96.minesweeper.Configuration;
import com.gmail.likhachev96.minesweeper.Minesweeper;
import com.gmail.likhachev96.minesweeper.controllers.BoardController;
import com.gmail.likhachev96.minesweeper.models.Board;
import com.gmail.likhachev96.minesweeper.models.Cell;
import com.gmail.likhachev96.minesweeper.models.Score;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class SwingBoardView
        extends JFrame
        implements IBoardView {
    private static final int CELL_SIZE = 30;

    private Board board;
    private BoardController controller;
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JButton[][] boardButtons;
    private JButton newGameButton;

    private JLabel flagsCounter = new JLabel();
    private JLabel emptyContainer = new JLabel();

    public SwingBoardView(BoardController controller) throws IOException {
        this.controller = controller;
        this.controller.addView(this);
        this.initializeWindow();
        this.initializeMenuBar();
        this.pack();
        this.controller.onViewInitialized(this);
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
        this.flagsCounter.setIcon(UIAssets.getNumber(this.board.getMinesCount()));
        this.gamePanel.setPreferredSize(new Dimension(CELL_SIZE * this.board.getWidth(), CELL_SIZE * this.board.getHeight()));
        if (this.gamePanel.getLayout() instanceof GridLayout) {
            ((GridLayout) this.gamePanel.getLayout()).setColumns(this.board.getWidth());
            ((GridLayout) this.gamePanel.getLayout()).setRows(this.board.getHeight());
        } else {
            throw new RuntimeException("Wrong layout manager of game board! Expected: java.awt.GridLayout, got: "
                    + this.gamePanel.getLayout().getClass());
        }
        this.setTitle(Minesweeper.TITLE
                        .replace("%n%", String.valueOf(board.getWidth()))
                        .replace("%m%", String.valueOf(board.getHeight()))
        );
        this.gamePanel.removeAll();
        this.boardButtons = new JButton[this.board.getWidth()][this.board.getHeight()];
        for (int y = 0; y < this.board.getHeight(); y++) {
            for (int x = 0; x < this.board.getWidth(); x++) {
                this.boardButtons[x][y] = new JButton();

                this.boardButtons[x][y].setFocusPainted(false);
                this.boardButtons[x][y].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                this.boardButtons[x][y].setIcon(CellsAssets.getUnrevealedIcon());

                this.boardButtons[x][y].setBorder(BorderFactory.createEmptyBorder());
                this.boardButtons[x][y].setContentAreaFilled(true);

                this.boardButtons[x][y].addMouseListener(new SwingCellListener(this, x, y));
                this.gamePanel.add(this.boardButtons[x][y], y, x);
            }
        }
        this.pack();
        this.setLocationRelativeTo(null); // centering
    }

    @Override
    public void redraw() {
        int flagsSet = 0;
        if (this.board.isGameOver() && !this.board.isGameWon()) {
            newGameButton.setIcon(UIAssets.NEW_GAME_GAMEOVER);
        }
        for (int y = 0; y < this.board.getHeight(); y++) {
            for (int x = 0; x < this.board.getWidth(); x++) {
                Cell cell = this.controller.getCell(x, y);

                if (cell.isMarked()) {
                    ++flagsSet;
                    if (!this.board.isGameOver()) this.boardButtons[x][y].setIcon(CellsAssets.getFlagIcon());
                    else this.boardButtons[x][y].setIcon(CellsAssets.getFlagIcon(cell.isMine()));
                } else if (cell.isMine() && (cell.isRevealed() || board.isGameOver())) {
                    if (board.isGameOver() && (cell.isRevealed() || cell.isMarked())) {
                        this.boardButtons[x][y].setIcon(CellsAssets.getMineIcon(cell.isRevealed()));
                    } else this.boardButtons[x][y].setIcon(CellsAssets.getMineIcon());
                } else if (cell.isRevealed()) {
                    this.boardButtons[x][y].setIcon(CellsAssets.getNumber(cell.countNearestMines()));
                } else {
                    this.boardButtons[x][y].setIcon(CellsAssets.getUnrevealedIcon());
                }
            }
        }
        flagsCounter.setIcon(UIAssets.getNumber(this.board.getMinesCount() - flagsSet));
    }

    private void initializeWindow() throws IOException {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(false);
        GridLayout gameLayout = new GridLayout();
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        this.gamePanel = new JPanel(gameLayout);

        // New game button
        newGameButton = createNewGameButton();
        newGameButton.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(newGameButton, BorderLayout.CENTER);
        mainPanel.add(flagsCounter, BorderLayout.LINE_START);
        emptyContainer.setIcon(UIAssets.EMPTY);
        mainPanel.add(emptyContainer, BorderLayout.LINE_END);
        newGameButton.setSize(30, 30);
        // /New game button

        mainPanel.add(gamePanel, BorderLayout.PAGE_END);

        this.add(mainPanel);

        this.setIconImage(CellsAssets.getMineIcon().getImage());
    }

    private JButton createNewGameButton() throws IOException {
        JButton button = new JButton(UIAssets.NEW_GAME);
        button.setFocusPainted(false);
        button.addMouseListener(new MouseAdapter() {
            private Icon staticIcon;
            private boolean hovered;
            private boolean pressed;

            @Override
            public void mousePressed(MouseEvent e) {
                button.setIcon(UIAssets.NEW_GAME_CLICK);
                this.pressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (this.hovered) {
                    button.setIcon(UIAssets.NEW_GAME_HOVER);
                    this.staticIcon = UIAssets.NEW_GAME;
                } else {
                    button.setIcon(this.staticIcon);
                }
                this.pressed = false;
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!this.pressed) {
                    this.staticIcon = button.getIcon();
                    button.setIcon(UIAssets.NEW_GAME_HOVER);
                }
                this.hovered = true;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!this.pressed) {
                    button.setIcon(this.staticIcon);
                }
                this.hovered = false;
            }
        });
        button.addActionListener(event -> this.controller.startNewGame());
        button.setContentAreaFilled(false);
        return button;
    }

    private void initializeMenuBar() {
        JMenuBar menu = new JMenuBar();

        JMenu menuGame = new JMenu("Game");

        JMenuItem menuGameNew = new JMenuItem("New game");
        JMenuItem menuGameHighScores = new JMenuItem("High scores");
        JMenuItem menuGameExit = new JMenuItem("Exit");
        menuGameNew.setAccelerator(KeyStroke.getKeyStroke("control N"));
        menuGameHighScores.setAccelerator(KeyStroke.getKeyStroke("control H"));
        menuGameExit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        menuGameNew.addActionListener((event) -> controller.startNewGame());
        menuGameHighScores.addActionListener((event) -> controller.showHighScores());
        menuGameExit.addActionListener((event) -> controller.exit());
        menuGame.add(menuGameNew);
        menuGame.add(menuGameHighScores);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menu.add(menuGame);

        JMenu menuDifficulty = new JMenu("Difficulty");
        JMenuItem menuDifficultyEasy = new JMenuItem("Easy");
        menuDifficultyEasy.addActionListener((event) -> controller.startNewGame(Configuration.EASY));
        JMenuItem menuDifficultyMedium = new JMenuItem("Medium");
        menuDifficultyMedium.addActionListener((event) -> controller.startNewGame(Configuration.MEDIUM));
        JMenuItem menuDifficultyHard = new JMenuItem("Hard");
        menuDifficultyHard.addActionListener((event) -> controller.startNewGame(Configuration.HARD));
        menuDifficulty.add(menuDifficultyEasy);
        menuDifficulty.add(menuDifficultyMedium);
        menuDifficulty.add(menuDifficultyHard);
        menu.add(menuDifficulty);

        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuHelpAbout = new JMenuItem("About");
        menuHelpAbout.setAccelerator(KeyStroke.getKeyStroke("control A"));
        menuHelpAbout.addActionListener((event) -> controller.showAbout());
        menuHelp.add(menuHelpAbout);
        menu.add(menuHelp);

        this.setJMenuBar(menu);
    }

    public BoardController getController() {
        return controller;
    }

    @Override
    public void showAbout() {
        JOptionPane.showMessageDialog(null, "Minesweeper v1.0\nDeveloper: Alexander Likhachev\ne-mail: likhachev96@gmail.com", "About", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void showHighScores(List<Score> scores) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (Score score : scores) {
            builder.append(score.getUsername()).append(": ").append(score.getTimeInSeconds()).append('\n');
            if (++i == 10) break; // show only first 10
        }
        if (scores.isEmpty()) {
            builder.append("No scores yet.");
        }
        JOptionPane.showMessageDialog(null, "High scores:\n" + builder.toString(), "High scores", JOptionPane.PLAIN_MESSAGE);
    }
}
