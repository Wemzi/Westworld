package Model.Blocks;

import Model.Position;
import View.spriteManagers.SpriteManager;

import java.awt.*;

/**
 * Kuka osztály. Utakra lehet őket helyezni, és ezáltal a látogatók nem dobnak oda szemetet.
 */
public class GarbageCan extends Road{
    private static SpriteManager spriteManager;
    public GarbageCan(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, boolean hasGarbageCan, boolean isEntrance, int garbage) {
        super(buildingCost, upkeepCost, popularityIncrease, state, hasGarbageCan, isEntrance, garbage);
    }

    public GarbageCan(Position p) {
        super(p);
    }

    public GarbageCan(Position p,  boolean isEntrance) {
        super(p, true, isEntrance);
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public String getName(){return "Garbage Can";}

    @Override
    protected SpriteManager getSpriteManager() {
        super.getSpriteManager();
        return garbageCanSpriteManager;
    }
}
