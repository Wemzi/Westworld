package Model.Blocks;

import Model.Blocks.Block;
import Model.Blocks.BlockState;

public class Decoration  extends Block {
    public Decoration(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
    }
}
