package Model.Blocks;

import Model.People.Cleaner;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.Objects;

/**
 * Az út, ami összeköt. Ezeken mozognak a Visitorok, így érik el céljaikat, itt keresnek újabb kihívásokat.
 */
public class Road extends Block {
    /**
     * Szemét szinteket tartalmazó enumeráció.
     */
    public enum GarbageLevel{NONE,FEW,LOT};
    private static SpriteManager noGarbageSpriteManager =null;
    private static SpriteManager fewGarbageSpriteManager =null;
    private static SpriteManager lotGarbageSpriteManager =null;
    protected static SpriteManager garbageCanSpriteManager =null;
    private static SpriteManager entranceSpriteManager =null;
    private boolean hasGarbageCan;
    private boolean isEntrance;
    private int garbage;
    public Cleaner cleaner=null;

    /**
     * Út osztály konstruktora.
     * @param buildingCost Az építés ára
     * @param upkeepCost a fenntartás ára
     * @param popularityIncrease ennyivel növeli a népszerűséget
     * @param state az állapota
     * @param hasGarbageCan van-e rajta kuka
     * @param isEntrance bejárat-e
     * @param garbage mennyi szemetet tartalmaz
     */
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

    /**
     *
     * @return Hogy van e az úton kuka.
     */
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

    /**
     * A garbage számot GarbageLevel-é konvertáljuk.
     * @return Semennyi, kevés, vagy sok szemét van.
     */
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

    /**
     * Megkeresi az út állapotának megfelelő spriteot, és beállítja azt.
     * @return
     */
    @Override
    protected SpriteManager getSpriteManager() {
        if(Objects.isNull(noGarbageSpriteManager)){
            noGarbageSpriteManager =new StaticSpriteManager("graphics/stone.png",getSize());
            fewGarbageSpriteManager =new StaticSpriteManager("graphics/stoneg1.png",getSize());
            lotGarbageSpriteManager =new StaticSpriteManager("graphics/stoneg2.png",getSize());
            garbageCanSpriteManager =new StaticSpriteManager("graphics/stonegarbagecan.png",getSize());
            entranceSpriteManager =new StaticSpriteManager("graphics/stoneentrance.png",getSize());
        }
        if(hasGarbageCan){return garbageCanSpriteManager ;}
        if(isEntrance){return entranceSpriteManager ;}
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
