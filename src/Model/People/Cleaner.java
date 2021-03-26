package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Road;
import Model.Position;

import java.awt.*;

public class Cleaner extends Employee {

    public Cleaner(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public void clean(Road b )
    {
        currentActivityLength = 25;
        b.setGarbage(0);
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(currentActivityLength == 0)
        {
            // go clean smth
        }
        else
        {
            currentActivityLength-= minutesPerSecond;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.MAGENTA;};
}
