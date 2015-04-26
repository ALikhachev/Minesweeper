package assets.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public final class UIAssets {
  public static ImageIcon NEW_GAME;
  public static ImageIcon NEW_GAME_HOVER;
  public static ImageIcon NEW_GAME_CLICK;
  public static ImageIcon NEW_GAME_GAMEOVER;
  public static ImageIcon EMPTY;

  private static ImageIcon[] NUMBERS = new ImageIcon[100];


  static {
    try {
      int CELL_SIZE = 30;
      NEW_GAME = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
      NEW_GAME_HOVER = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng_hover.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
      NEW_GAME_CLICK = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng_click.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
      NEW_GAME_GAMEOVER = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng_gameover.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
      EMPTY = new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("empty.png")));

      for (int i = 0; i < 10; i++) {
        NUMBERS[i] = new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream(i + ".png")));
      }
      ImageIcon[] cpy = Arrays.copyOf(NUMBERS, 10);
      for (int i = 0; i < NUMBERS.length; i++) {
        NUMBERS[i] = concat(new ArrayList<>(Arrays.asList(cpy[i / 10], cpy[i % 10])));
      }
    } catch (IOException e) {
      throw new RuntimeException("Cannot initialize icons!");
    }
  }

  private UIAssets() {
  }

  public static ImageIcon getNumber(int i) {
    if (i < 0) i = 0;
    if (i > 99) i = 99;
    return NUMBERS[i];
  }

  private static ImageIcon concat(ArrayList<ImageIcon> images) {
    BufferedImage dest = null;
    Graphics2D destG = null;

    for (int i = 0, size = images.size(); i < size; i++) {
      Image image = images.get(i).getImage();

      if (i == 0) {
        dest = new BufferedImage(image.getWidth(null) * images.size(), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        destG = dest.createGraphics();
      }

      destG.drawImage(image, i * image.getWidth(null), 0, null);
    }

    return dest != null ? new ImageIcon(dest) : null;
  }
}
