package View.spriteManagers;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StaticSpriteManager extends SpriteManager {
    private final BufferedImage img;

    public StaticSpriteManager(String imgPath, Position blockSize) {
        super(blockSize);
        BufferedImage i= null;
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
