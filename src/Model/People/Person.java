package Model.People;

import Model.Blocks.Block;
import Model.Position;

import java.awt.*;

abstract public class Person {
    // TODO: delete Block position & only calculate position by pixels
    private Block posBlock;
    private Position posCoord;
    protected Person(Position startingCoord, Block startingBlock)
    {
        posCoord = startingCoord;
        posBlock = startingBlock;

    }

    private void moveTo(Block to)
    {
        posBlock = to;
    }

    public Block getPosition() {return posBlock;}
    public Position getCoord() {return posCoord;}
    public void setPosition(Position that)
    {
        posCoord = that;
    }

     protected Color getColor(){return Color.white;};


}
