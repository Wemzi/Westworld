package Model.Blocks;

import Model.Position;
import View.MainWindow2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FreePlace extends Block {
    private static BufferedImage img=null;

    public FreePlace(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        try {
            BufferedImage i=ImageIO.read(new File("graphics/grass.png"));
            img=resize(i, MainWindow2.BOX_SIZE,MainWindow2.BOX_SIZE);
        } catch (IOException e) {
            System.err.println("grass.png not found");
        }
    }

    public FreePlace(Position p){
        super(p);
        try {
            BufferedImage i=ImageIO.read(new File("graphics/grass.png"));
            img=resize(i, MainWindow2.BOX_SIZE,MainWindow2.BOX_SIZE);
        } catch (IOException e) {
            System.err.println("grass.png not found");
        }
    }

    @Override
    public void paint(Graphics2D gr) {
        if(Objects.isNull(img)){
            gr.setColor(getColor());
            gr.fillRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
        }else{
            gr.drawImage(img,pos.getX_asPixel(),pos.getY_asPixel(),null);
        }
        gr.setColor(Color.BLACK);
        gr.drawRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
    }

    @Override
    public Color getColor() {
        return Color.white;
    }

    @Override
    public String toString() {
        return "FreePlace{}" + super.toString();
    }

    @Override
    public String getName()
    { return "Free place";
    }

}
