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
    private int stayingTime;
// TODO: add a new value that defines how long are they staying. make it changeable.
    // TODO: cyclical waiting at a game for example
    // TODO: change playfulness and hunger in time
    // TODO: they shouldnt interrupt their actions

    public Visitor(Position startingPos)
    {
        super(startingPos);
        happiness = 50;
        hunger = 0;
        playfulness = 50;
        stayingTime = 300;
    }

    public void playGame(Game that)
    {
        playfulness -= 50;
        happiness += 20;
        hunger += 15;
        currentActivityLength = that.getCooldownTime();
    }
    public void eat(ServiceArea where)
    {
        hunger = 0;
        happiness += 5 ;
        playfulness += 30;
        currentActivityLength = where.getCooldownTime();
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

    public void setStayingTime(int stayingTime) {
        this.stayingTime = stayingTime;
    }

    public int getStayingTime() { return stayingTime; }


    @Override
    protected Color getColor(){return Color.pink;}

    public void roundHasPassed(){
        this.hunger += 1;
        if(hunger < 50)
        {
            this.playfulness +=2;
        }
        if(this.currentActivityLength == 0)
        {
            happiness-- ;
        }
        else
        {
            currentActivityLength --;
        }

        if(stayingTime == 0)
        {
            // TODO: leave the playground
        }
        else
        {
            stayingTime--;
        }
        return;
    }

    public int getCurrentActivityLength() {
        return currentActivityLength;
    }
}
