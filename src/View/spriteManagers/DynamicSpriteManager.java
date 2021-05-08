package View.spriteManagers;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Több fázisú spritok kezelésére szolgál.
 * Minden sprite külön fájlban van.
 */
public class DynamicSpriteManager extends SpriteManager {
    private final LinkedList<BufferedImage> sprites;
    private int actualSpriteIndex;
    private final int timePeriod;
    private int timeCounter;

    /**
     *
     * @param spritePathes - a képeket tartalmzó fájlok elérési útja. Sorrendben.
     * @param blockSize - a kép kívánt mérete
     * @param timePeriod - a fázisváltások közti idő
     */
    public DynamicSpriteManager(List<String> spritePathes, Position blockSize,int timePeriod) {
        this.actualSpriteIndex =0;
        this.sprites = new LinkedList<>();
        for(String path : spritePathes){
            try {
                BufferedImage i = ImageIO.read(new File(path));
                sprites.add(resize(i, blockSize.getX_asPixel(), blockSize.getY_asPixel()));
            } catch (IOException e) {
                System.err.println(path+" not found");
            }
        }

        this.timeCounter=timePeriod;
        this.timePeriod=timePeriod;
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
