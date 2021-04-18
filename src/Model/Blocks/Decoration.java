package Model.Blocks;

import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.HashMap;


public class Decoration  extends Block {
    private final DecType decorationType;
    private static final HashMap<DecType, SpriteManager> spriteMap=new HashMap<>();

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

    private void setupImage(){
        if(spriteMap.containsKey(decorationType)){return;}
        System.out.println(decorationType);
        switch (decorationType) {
            case JUNGLETREE:
                spriteMap.put(decorationType,new StaticSpriteManager("graphics/Tree.png",getSize()));
                break;
            case LAKE:
                spriteMap.put(decorationType,new StaticSpriteManager("graphics/lake.png",getSize()));
                break;
            case FLOWERGARDEN:
                spriteMap.put(decorationType,new StaticSpriteManager("graphics/flowgarden.png",getSize()));
                break;
            default:
                spriteMap.put(decorationType,new OneColorSpriteManager(getColor(),getSize()));
                break;
        }
    }

    @Override
    protected SpriteManager getSpriteManager() {
        setupImage();
        return spriteMap.get(decorationType);
    }
}