package View;

import Model.Position;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class SpriteManager {
    public abstract BufferedImage nextSprite();

    public static BufferedImage resize(BufferedImage img, Position newSize){return resize(img,newSize.getX_asPixel(), newSize.getY_asPixel());}
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
