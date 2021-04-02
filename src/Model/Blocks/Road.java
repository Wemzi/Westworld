package Model.Blocks;

import Model.Position;
import View.MainWindow2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Road extends Block {
    private static BufferedImage img=null;
    private boolean hasGarbageCan;
    private boolean isEntrance;
    private int garbage;


    public Road(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, boolean hasGarbageCan, boolean isEntrance, int garbage) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.hasGarbageCan = hasGarbageCan;
        this.isEntrance = isEntrance;
        this.garbage = garbage;
        this.buildingCost = 100;
        this.upkeepCost = 0;
        setupImage();
    }

    public Road(Position p){
        super(0,10,0,BlockState.FREE, new Position(1,1,false), p);
        hasGarbageCan=false;
        isEntrance=false;
        garbage=0;
    }
    public Road(Position p,boolean hasGarbageCan, boolean isEntrance){
        super(0,10,0,BlockState.FREE, new Position(1,1,false), p);
        this.hasGarbageCan=hasGarbageCan;
        this.isEntrance=isEntrance;
        garbage=0;
    }

    //getters setters
    public boolean isHasGarbageCan() {
        return hasGarbageCan;
    }

    public void setHasGarbageCan(boolean hasGarbageCan) {
        this.hasGarbageCan = hasGarbageCan;
    }

    public boolean isEntrance() {
        return isEntrance;
    }

    public void setEntrance(boolean entrance) {
        isEntrance = entrance;
    }

    public int getGarbage() {
        return garbage;
    }

    public void setGarbage(int garbage) {
        this.garbage = garbage;
    }



    @Override
    public Color getColor() {
        return isEntrance ? Color.DARK_GRAY : Color.GRAY;
    }

    @Override
    public String toString() {
        return "Road{" +
                "hasGarbageCan=" + hasGarbageCan +
                ", isEntrance=" + isEntrance +
                ", garbage=" + garbage +
                " " + super.toString();
    }

    @Override
    public String getName(){return "Road"; }

    private void setupImage(){
        String imgPath="graphics/stone.png";
        try {
            BufferedImage i= ImageIO.read(new File(imgPath));
            img=resize(i, MainWindow2.BOX_SIZE,MainWindow2.BOX_SIZE);
        } catch (IOException e) {
            System.err.println(imgPath+" not found");
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
}
