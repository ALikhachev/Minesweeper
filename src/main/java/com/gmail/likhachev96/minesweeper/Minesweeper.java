package com.gmail.likhachev96.minesweeper;

import com.gmail.likhachev96.minesweeper.controllers.BoardController;
import com.gmail.likhachev96.minesweeper.views.SwingBoardView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class Minesweeper {
  public final static Logger LOGGER = LogManager.getLogger();
  public final static String TITLE = "Minesweeper (%n%x%m%)";

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      BoardController controller = new BoardController();
      SwingBoardView view = new SwingBoardView(controller);
      view.setVisible(true);
    } catch (Exception ex) {
      LOGGER.fatal("Fatal error occurred during execution", ex);
      System.exit(0);
    }
  }
}
