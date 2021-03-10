package Model.Blocks;

import java.awt.*;

public class FreePlace extends Block {
    public FreePlace(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
    }

    @Override
    public Color getColor() {
        return Color.white;
    }

    @Override
    public String toString() {
        return "FreePlace{}" + super.toString();
    }
}
