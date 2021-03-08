package Model.Blocks;

import Model.Blocks.Block;
import Model.Blocks.BlockState;

public class FreePlace extends Block {
    public FreePlace(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
    }
}
