package Model.People;

import Model.Blocks.Block;
import Model.Position;

import java.awt.*;

public class Cleaner extends Employee {

    public Cleaner(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public void clean(Block b )
    {
        this.setIsBusy(true);
        //b.garbage = 0;
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);

    }

    public void roundHasPassed()
    {
        if(currentActivityLength == 0)
        {
            // go clean smth
            currentActivityLength = 3;
        }
        else
        {
            currentActivityLength--;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.MAGENTA;};
}
