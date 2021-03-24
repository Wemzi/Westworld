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
        g.setCondition(100);
         // TODO: this should be handled in the playground?
        this.setIsBusy(false);

    }

    public void roundHasPassed()
    {
        if(this.currentActivityLength==0)
        {
            // go repair smth
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.yellow;};
}
