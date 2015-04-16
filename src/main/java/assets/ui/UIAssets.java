package assets.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public final class UIAssets {
    public static ImageIcon NEW_GAME;
    public static ImageIcon NEW_GAME_HOVER;
    public static ImageIcon NEW_GAME_CLICK;
    public static ImageIcon NEW_GAME_GAMEOVER;

    static {
        try {
            int CELL_SIZE = 30;
            NEW_GAME = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            NEW_GAME_HOVER = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng_hover.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            NEW_GAME_CLICK = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng_click.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            NEW_GAME_GAMEOVER = new ImageIcon(new ImageIcon(ImageIO.read(UIAssets.class.getResourceAsStream("ng_gameover.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize icons!");
        }
    }

    private UIAssets() {
    }
}
