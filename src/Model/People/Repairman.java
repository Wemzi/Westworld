package Model.People;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.Game;
import Model.Position;

import java.awt.*;

public class Repairman extends Employee {

    public Repairman(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public void repair(Game g ) {
        if (g.getState().equals(BlockState.FREE)) {
            g.setState(BlockState.UNDER_REPAIR);
            g.setCondition(100);
            currentActivityLength = g.getBuildingTime() / 5;
            g.setCooldownTime(currentActivityLength);
        }
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(this.currentActivityLength==0)
        {
            // go repair smth
        }
        else
        { currentActivityLength -= minutesPerSecond; }
        return;
    }

    @Override
    protected Color getColor(){return Color.yellow;};
}
