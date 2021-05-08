package View.spriteManagers;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Egy block egyetlen fázisból álló képének kezelését végzi.
 */
public class StaticSpriteManager extends SpriteManager {
    private final BufferedImage img;

    /**
     * @param imgPath - a kép elérési útja
     * @param blockSize - A kép kívánt mérete
     */
    public StaticSpriteManager(String imgPath, Position blockSize) {
        BufferedImage i= (new OneColorSpriteManager(Color.black,blockSize).nextSprite());
        try {
            i = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            System.err.println(imgPath+" not found");
        }
        img=resize(i, blockSize.getX_asPixel(), blockSize.getY_asPixel());
    }

    @Override
    public BufferedImage nextSprite() {
        return img;
    }
}
