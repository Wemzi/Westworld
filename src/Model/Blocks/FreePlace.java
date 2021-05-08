package Model.Blocks;

import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.Objects;

/**
 * Ezt az osztályt használjuk arra a célra, ha valami üres. Alapból ilyennel lesz tele a pályánk.
 */
public class FreePlace extends Block {
    private static SpriteManager mySpMan;


    public FreePlace(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
    }

    public FreePlace(Position p){
        super(p);
    }


    @Override
    protected SpriteManager getSpriteManager() {
        if(Objects.isNull(mySpMan)){mySpMan=new StaticSpriteManager("graphics/grass.png",size);}
        return mySpMan;
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
