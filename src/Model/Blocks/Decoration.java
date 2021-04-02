package Model.Blocks;

import Model.Position;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class Decoration  extends Block {
    private DecType decorationType;
    private static final HashMap<DecType,BufferedImage> imgMap=new HashMap<>();

    @Deprecated
    public Decoration(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.decorationType = DecType.BUSH;
        setupImage();
    }

    public Decoration(DecType type,Position pos){
        this.decorationType = type;
        if(type == DecType.BUSH)
        {
            this.buildingCost = 50;
            this.upkeepCost = 8;
            this.popularityIncrease=1.1;
            this.size = new Position(1,1,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.JUNGLETREE){
            this.buildingCost = 80;
            this.upkeepCost = 10;
            this.popularityIncrease=1.12;
            this.size = new Position(1,1,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.FLOWERGARDEN){
            this.buildingCost = 150;
            this.upkeepCost = 20;
            this.popularityIncrease=1.3;
            this.size = new Position(2,3,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.LAKE){
            this.buildingCost = 200;
            this.upkeepCost = 40;
            this.popularityIncrease=1.2;
            this.size = new Position(2,2,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else if(type == DecType.MONKEYCAGE){
            this.buildingCost = 140;
            this.upkeepCost = 8;
            this.popularityIncrease=1.1;
            this.size = new Position(2,1,false);
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.pos=pos;
        }
        else throw new RuntimeException("Invalid type of decoration!");

        setupImage();
    }

    public DecType getDecorationType() {
        return decorationType;
    }

    @Override
    public Color getColor() {return Color.GREEN;}

    @Override
    public String toString() {
            return  "Decoration type: " + decorationType.toString() + " " + super.toString();
        }

    @Override
    public String getName()
    {
        switch(this.decorationType)
        {
            case MONKEYCAGE :return "Monkey Cage";
            case JUNGLETREE: return "Jungle Tree";
            case LAKE: return "Lake";
            case FLOWERGARDEN: return "Flower Garden";
            case BUSH : return "Bush";
            default : return "undefined";
        }
    }

    private String getImagePath(){
        switch (this.decorationType) {
            case JUNGLETREE:
                return "graphics/Tree.png";
            case LAKE:
                return "graphics/lake.png";
            default:
                return "undefined";
        }
    }

    private void setupImage(){
        String imgPath=getImagePath();
        try {
            if(!imgMap.containsKey(decorationType) && !imgPath.equals("undefined")){
                BufferedImage i= ImageIO.read(new File(imgPath));
                BufferedImage img=resize(i, size.getX_asPixel(),size.getY_asPixel());
                imgMap.put(decorationType,img);
            }
        } catch (IOException e) {
            System.err.println(imgPath+" not found");
        }
    }

    @Override
    public void paint(Graphics2D gr) {
        if(!imgMap.containsKey(decorationType)){
            gr.setColor(getColor());
            gr.fillRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
        }else{
            gr.drawImage(imgMap.get(decorationType),pos.getX_asPixel(),pos.getY_asPixel(),DEFAULT_BACKGROUNG_COLOR,null);
        }
        gr.setColor(Color.BLACK);
        gr.drawRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
    }
}