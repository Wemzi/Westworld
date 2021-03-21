package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Game;
import Model.Position;

import java.awt.*;

public class Operator extends Employee {
    private Game operateThis;

    public Operator(Position startingPos, int salary, Game operateThis)
    {
        super(startingPos,salary);
        this.operateThis = operateThis;
    }

    public void operate(Game g )
    {
        this.setIsBusy(true);
        // TODO: this should be handled in the playground?
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
