package View.spriteManagers;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Több fázisú spritok kezelésére szolgál.
 * Minden sprite egy fájlban van.
 */
public class OnePicDynamicSpriteManager extends SpriteManager {
    final private List<BufferedImage> sprites;
    private int actualSpriteIndex = 0;
    private final int timePeriod;
    private int timeCounter;

    /**
     *
     * @param imgPath - a kép fájl elérési útja
     * @param blockSize  - a kép kívánt mérete
     * @param rectangles - a kép részleteket befoglaló téglalapok sorrendben
     * @param timePeriod - a fázisváltások közti idő
     */
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
        if(isStopped){return sprites.get(actualSpriteIndex);}
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

}