package Model.People;

import Model.Blocks.Block;
import Model.Position;

import java.awt.*;

abstract public class Person {
    private Position pos;
    protected int currentActivityLength;
    protected Person(Position startingCoord)
    {
        pos= startingCoord;
        currentActivityLength = 0;
    }

    private void moveTo(Block to)
    {
        pos = to.getPos();
    }
    public Position getPosition() {return pos;}
    public void setPosition(Position that)
    {
        pos= that;
    }
    protected Color getColor(){return Color.white;};
    abstract protected void roundHasPassed(int minutesPerSecond);
    protected boolean isBusy(){return currentActivityLength!=0;}


}
