package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Game;
import Model.Position;

import java.awt.*;

public class Operator extends Employee {
    private Game operateThis;

    @Deprecated
    public Operator(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public Operator(Position startingPos, int salary, Game operateThis)
    {
        super(startingPos,salary);
        this.operateThis = operateThis;
    }

    public void operate()
    {
        operateThis.run();
        currentActivityLength = operateThis.getCooldownTime();
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(currentActivityLength == 0 && operateThis.getQueue().remainingCapacity()==0)
        {
            this.operate();
        }
        else
        {
            currentActivityLength-=minutesPerSecond;
        }
        if(currentActivityLength <= 0 )
        {
            currentActivityLength = 0;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.cyan;};
}
