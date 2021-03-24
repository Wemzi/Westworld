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

    public void operate(Game g )
    {
        this.setIsBusy(true);
        currentActivityLength = g.getCooldownTime();
        this.setIsBusy(false);
    }

    public void roundHasPassed()
    {
        if(currentActivityLength == 0)
        {
            this.operate(operateThis);
            currentActivityLength = operateThis.getCooldownTime();
        }
        else
        {
            currentActivityLength--;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.cyan;};
}
