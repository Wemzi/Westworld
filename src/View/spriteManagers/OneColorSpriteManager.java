package View.spriteManagers;

import Model.Position;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

public class OneColorSpriteManager extends SpriteManager {
    private final BufferedImage img;

    public OneColorSpriteManager(Color c, Position blockSize) {
        img = new BufferedImage(blockSize.getX_asPixel(), blockSize.getY_asPixel(), ColorSpace.TYPE_RGB);
        Graphics2D graphics = img.createGraphics();

        // Fill the background with color
        graphics.setColor (c);
        graphics.fillRect ( 0, 0, blockSize.getX_asPixel(), blockSize.getY_asPixel());
    }

    @Override
    public BufferedImage nextSprite() {
        return img;
    }
}
