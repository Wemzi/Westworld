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

    public Visitor(Position startingPos)
    {
        super(startingPos);
        happiness = 50;
        hunger = 0;
        playfulness = 50;
    }

    public void playGame(Game that)
    {
        // TODO: this should be done by the playground, or the player? I think its the playground
        playfulness -= 50;
        happiness += 20;
        hunger += 15;
    }
    public void eat(ServiceArea where)
    {
        // TODO: this should be done by the playground, or the player? I think its the playground
        hunger = 0;
        happiness += 5 ;
        playfulness += 30;
        //throwGarbage(posBlock);
    }

    public Road throwGarbage(Road there)
    {
        // TODO: need to see exact implementation of this in Road Class
        //there.garbage
        return there;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getHunger() { return hunger; }

    public int getPlayfulness() {
        return playfulness;
    }

    @Override
    protected Color getColor(){return Color.pink;};
}
