package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Game;
import Model.Blocks.Road;
import Model.Blocks.ServiceArea;
import Model.Position;

import java.awt.*;

public class Visitor extends Person {
    private int happiness;
    private int hunger;
    private int playfulness;

    public Visitor(Position startingCoord, Block startingBlock)
    {
        super(startingCoord,startingBlock);
    }

    public void playGame(Game that)
    {
        // TODO: this should be done by the playground, or the player? I think its the playground
    }
    public void eat(ServiceArea where)
    {
        // TODO: this should be done by the playground, or the player? I think its the playground
        hunger = 0;
        playfulness += 30;
        //throwGarbage(posBlock);
    }

    public Road throwGarbage(Road there)
    {
        // TODO: need to see exact implementation of this in Road Class
        //there.garbage
        return there;
    }
    @Override
    protected Color getColor(){return Color.pink;};
}
