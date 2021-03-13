package Model.People;

import Model.Blocks.Block;
import Model.Position;

import java.awt.*;

public class Cleaner extends Employee {

    public Cleaner(Position startingCoord, Block startingBlock, int salary)
    {
        super(startingCoord,startingBlock,salary);
    }

    public void clean(Block b )
    {
        this.setIsBusy(true);
        //b.garbage = 0;
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);

    }
    @Override
    protected Color getColor(){return Color.MAGENTA;};
}
