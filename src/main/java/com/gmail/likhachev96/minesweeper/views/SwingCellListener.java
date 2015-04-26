package com.gmail.likhachev96.minesweeper.views;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public class SwingCellListener implements MouseListener {
  private final IBoardView view;
  private final int x;
  private final int y;

  private boolean cancelled = false;

  public SwingCellListener(IBoardView view, int x, int y) {
    this.view = view;
    this.x = x;
    this.y = y;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.view.redraw();
  }

  @Override
  public void mousePressed(MouseEvent e) {
    this.view.redraw();
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (!this.cancelled) {
      if (SwingUtilities.isLeftMouseButton(e)) {
        this.view.getController().click(this.x, this.y);
      } else if (SwingUtilities.isRightMouseButton(e)) {
        this.view.getController().flag(this.x, this.y);
      }
    }
    this.view.redraw();
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    this.cancelled = false;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    this.cancelled = true;
  }
}
