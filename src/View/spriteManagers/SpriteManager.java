package View.spriteManagers;

import Model.Position;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A kpek kezelésére szolgáló osztály
 */
public abstract class SpriteManager {
    protected boolean isStopped=true;

    /** Amennyiben érhető el mozgó animáció, elindítja azt */
    public void start(){isStopped=false;}

    /** Amennyiben érhető el mozgó animáció, leállítja azt */
    public void stop(){isStopped=true;}

    /** A következő spritera ugrik.*/
    public abstract BufferedImage nextSprite();

    /**
     * Átméretezi a megadott képet.
     * @param img - átméretezendő kép
     * @param newSize - A kívánt méret pixelben
     * @return - az átméretezett kép.
     */
    public static BufferedImage resize(BufferedImage img, Position newSize){return resize(img,newSize.getX_asPixel(), newSize.getY_asPixel());}
    /**
     * Átméretezi a megadott képet.
     * @param img - átméretezendő kép
     * @param newW - A kívánt szélesség pixelben
     * @param newH - A kívánt magasság pixelben
     * @return - az átméretezett kép.
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     * Kivág egy képből egy részletet
     * @param src - forrás, amiből ki kell vágni
     * @param rect - A kivágandó téglalap
     * @return - A kivágott kép
     */
    BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        return src.getSubimage(rect.x,rect.y, rect.width, rect.height);
    }
}
