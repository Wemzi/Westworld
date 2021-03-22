package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Game;
import Model.Position;

import java.awt.*;

public class Repairman extends Employee {

    public Repairman(Position startingPos, int salary)
    {
        super(startingPos,salary);
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
