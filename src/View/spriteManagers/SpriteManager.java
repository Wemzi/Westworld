package View.spriteManagers;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public abstract class SpriteManager {
    private static BufferedImage workingPic;
    private static HashMap<Position,BufferedImage> picMap=new HashMap<>();
    private final Position blockSize;
    protected SpriteManager(Position blockSize) {
        if(Objects.isNull(workingPic)){
            try {
                workingPic=ImageIO.read(new File("graphics/work.png"));
            } catch (IOException e) {
                System.err.println(" not found");
            }
        }

        this.blockSize=blockSize;
        if(!picMap.containsKey(blockSize)){picMap.put(blockSize,SpriteManager.resize(workingPic,blockSize));}
    }

    public abstract BufferedImage nextSprite();
    public BufferedImage nextPausedSprite(){
        return picMap.get(blockSize);
    }

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
