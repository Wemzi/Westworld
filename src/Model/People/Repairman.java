package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Game;
import Model.Coord;

import java.awt.*;

public class Repairman extends Employee {

    public Repairman(Coord startingCoord, Block startingBlock, int salary)
    {
        super(startingCoord,startingBlock,salary);
    }

    public void repair(Game g ) {
        this.setIsBusy(true);
        //g.condition = 100;
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);

    }

    @Override
    protected Color getColor(){return Color.yellow;};
}
