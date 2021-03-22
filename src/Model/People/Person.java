package Model.People;

import Model.Blocks.Block;
import Model.Position;

import java.awt.*;

abstract public class Person {
    // TODO: delete Block position & only calculate position by pixels
    private Position pos;
    protected Person(Position startingCoord)
    {
        pos= startingCoord;
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


}
