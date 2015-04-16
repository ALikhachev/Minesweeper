package com.gmail.likhachev96.minesweeper;

import com.gmail.likhachev96.minesweeper.controllers.BoardController;
import com.gmail.likhachev96.minesweeper.views.SwingBoardView;

import javax.swing.*;
import java.io.IOException;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class Minesweeper {
    public final static String TITLE = "Minesweeper (%n%x%m%)";
    public static void main(String[] args) throws IOException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        javax.swing.UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        BoardController controller = new BoardController();
        SwingBoardView view = new SwingBoardView(controller);
        view.setVisible(true);
    }
}
