package assets.cells;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Copyright (c) 2015 Alexander Likhachev.
 */
public final class CellsAssets {
    private static ImageIcon UNREVEALED;

    private static ImageIcon[] MINES = new ImageIcon[3];
    private static ImageIcon[] FLAGS = new ImageIcon[3];

    private static ImageIcon[] NUMBERS = new ImageIcon[9];

    static {
        try {
            int CELL_SIZE = 30;
            UNREVEALED = new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream("unrevealed.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            ImageIcon REVEALED = new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream("revealed.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            ImageIcon EXPLODED = new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream("exploded.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            ImageIcon VALIDATED = new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream("validated.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            ImageIcon MINE = new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream("mine.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));
            ImageIcon FLAG = new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream("flag.png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH));

            NUMBERS[0] = REVEALED;
            for (int i = 1; i < NUMBERS.length; i++) {
                NUMBERS[i] = merge(new ArrayList<>(Arrays.asList(REVEALED, new ImageIcon(new ImageIcon(ImageIO.read(CellsAssets.class.getResourceAsStream(i + ".png"))).getImage().getScaledInstance(CELL_SIZE, CELL_SIZE, Image.SCALE_SMOOTH)))));
            }

            ImageIcon[] backgrounds = {UNREVEALED, VALIDATED, EXPLODED};
            for (int i = 0; i < backgrounds.length; i++) {
                ImageIcon back = backgrounds[i];
                MINES[i] = merge(new ArrayList<>(Arrays.asList(back, MINE)));
                FLAGS[i] = merge(new ArrayList<>(Arrays.asList(back, FLAG)));
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot initialize icons!");
        }
    }

    private CellsAssets() {
    }

    public static ImageIcon getUnrevealedIcon() {
        return UNREVEALED;
    }

    public static ImageIcon getMineIcon() {
        return MINES[0];
    }

    public static ImageIcon getMineIcon(boolean revealed) {
        return revealed ? MINES[2] : MINES[0];
    }

    public static ImageIcon getFlagIcon() {
        return FLAGS[0];
    }

    public static ImageIcon getFlagIcon(boolean wasMine) {
        return wasMine ? FLAGS[1] : FLAGS[2];
    }

    public static ImageIcon getNumber(int i) {
        return NUMBERS[i];
    }

    public static ImageIcon merge(ArrayList<ImageIcon> images) {
        List<Float> transparency = images.stream().map(i -> new Float(1.0)).collect(Collectors.toList());

        return merge(images, transparency);
    }

    public static ImageIcon merge(ArrayList<ImageIcon> images, List<Float> transparency)
    {
        BufferedImage dest = null;
        Graphics2D destG = null;
        int rule; // This is SRC for the top image, and DST_OVER for the other ones
        float alpha;

        for (int i = 0, size = images.size(); i < size; i++)
        {
            Image image = images.get(i).getImage();

            rule = AlphaComposite.SRC_OVER; // Default value
            alpha = transparency.get(i);

            if (i == 0)
            {
                dest = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
                destG = dest.createGraphics();

                rule = AlphaComposite.SRC; // Rule for 1st image
            }
            destG.setComposite(AlphaComposite.getInstance(rule, alpha));
            destG.drawImage(image, 0, 0, null);
        }

        return dest != null ? new ImageIcon(dest) : null;
    }
}
