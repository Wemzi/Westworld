package Model.Blocks;

import java.awt.*;

public class Decoration  extends Block {
    public Decoration(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
    }

    // TODO: Make different types of decorations

    @Override
    public Color getColor() {
        return Color.green;
    }

    @Override
    public String toString() {
        return  "Decoration{}" + super.toString();
    }
}
