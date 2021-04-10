package View;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class OnePicDynamicSpriteManager extends SpriteManager {
    final private List<BufferedImage> sprites;
    private int actualSpriteIndex = 0;
    private final int timePeriod;
    private int timeCounter;

    public OnePicDynamicSpriteManager(String imgPath, Position blockSize, List<Rectangle> rectangles, int timePeriod) {
        this.sprites= new LinkedList<>();
        try {
            BufferedImage whole = ImageIO.read(new File(imgPath));
            for(Rectangle r : rectangles){
                BufferedImage img=cropImage(whole,r);
                sprites.add(resize(img, blockSize.getX_asPixel(), blockSize.getY_asPixel()));
            }

        } catch (IOException e) {
            System.err.println(imgPath+" not found");
        }
        this.timePeriod = timePeriod;
        this.timeCounter = timePeriod;
    }

    @Override
    public BufferedImage nextSprite() {
        if(timeCounter>0){
            timeCounter--;
        }else{
            timeCounter=timePeriod;
            if(actualSpriteIndex == sprites.size()-1){
                actualSpriteIndex =0;}else{
                actualSpriteIndex++;}
        }
        return sprites.get(actualSpriteIndex);
    }

    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
        BufferedImage dest = src.getSubimage(rect.x,rect.y, rect.width, rect.height);
        return dest;
    }

}