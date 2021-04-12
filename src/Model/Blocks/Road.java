package Model.Blocks;

import Model.People.Cleaner;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.Objects;

public class Road extends Block {
    public enum GarbageLevel{NONE,FEW,LOT};
    private static SpriteManager noGarbageSpriteManager =null;
    private static SpriteManager fewGarbageSpriteManager =null;
    private static SpriteManager lotGarbageSpriteManager =null;
    private boolean hasGarbageCan;
    private boolean isEntrance;
    private int garbage;
    public Cleaner cleaner=null;


    public Road(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, boolean hasGarbageCan, boolean isEntrance, int garbage) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.hasGarbageCan = hasGarbageCan;
        this.isEntrance = isEntrance;
        this.garbage = garbage;
        this.buildingCost = 100;
        this.upkeepCost = 0;
    }

    public Road(Position p){
        this(p,false,false);
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
    public GarbageLevel getGarbageLevel(){
        if(garbage==0)return GarbageLevel.NONE;
        if(garbage<33)return GarbageLevel.FEW;
        return GarbageLevel.LOT;
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

    @Override
    protected SpriteManager getSpriteManager() {
        if(Objects.isNull(noGarbageSpriteManager)){
            noGarbageSpriteManager =new StaticSpriteManager("graphics/stone.png",getSize());
            fewGarbageSpriteManager =new OneColorSpriteManager(new Color(156, 73, 21),getSize());
            lotGarbageSpriteManager =new OneColorSpriteManager(new Color(40, 39, 39),getSize());
        }
        switch (getGarbageLevel()){
            case FEW :
                return fewGarbageSpriteManager;
            case LOT:
                return lotGarbageSpriteManager;
            default:
                return noGarbageSpriteManager;
        }
    }
}
