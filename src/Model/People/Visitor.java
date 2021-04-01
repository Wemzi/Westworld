package Model.People;

import Model.Blocks.Game;
import Model.Blocks.Road;
import Model.Blocks.ServiceArea;
import Model.Position;

import java.util.ArrayList;
import java.util.Random;

import java.awt.*;

public class Visitor extends Person {
    private ArrayList<Position> pathPosition;
    public boolean isMoving;
    public int pathPositionIndex;

    private int happiness;
    private int hunger;
    private int playfulness;
    private int stayingTime;
    private VisitorState state;
    // TODO: add a new value that defines how long are they staying. make it changeable.
    // TODO: cyclical waiting at a game for example
    // TODO: change playfulness and hunger in time
    // TODO: they shouldnt interrupt their actions
    // TODO: generate random numbers
    public Visitor(Position startingPos) {
        super(startingPos);
        Random rnd = new Random();
        happiness = rnd.nextInt() % 100;
        hunger = rnd.nextInt() % 100;
        playfulness = 50;
        stayingTime = rnd.nextInt() % 500;
        pathPosition = new ArrayList<>();
        isMoving = false;
        pathPositionIndex = 0;
        state = VisitorState.DOESNT_KNOW;
    }

    public void playGame(Game that) {
        playfulness -= 100;
        happiness += 20;
        hunger += 15;
        currentActivityLength = that.getCooldownTime();
    }

    public void eat(ServiceArea where) {
        hunger = 0;
        happiness += 5;
        playfulness += 50;
        currentActivityLength = where.getCooldownTime();
        state = VisitorState.WANNA_TOILET;
    }

    public void toilet(ServiceArea where) {
        currentActivityLength = where.getCooldownTime();
        state = VisitorState.WANNA_PLAY;
    }

    public Road throwGarbage(Road there) {
        there.setGarbage(there.getGarbage() + 2);
        return there;
    }

    // TODO : én beállítom úgy a stateket, hogy akar valamit csinálni, Alex pedig visszaállítja arra, hogy készen van
    public void roundHasPassed(int minutesPerSecond) {
        if(state.equals(VisitorState.WANNA_LEAVE) || state.equals(VisitorState.WANNA_TOILET))
        {
            return;
        }
        this.hunger += minutesPerSecond;
        if (hunger < 50) {
            this.playfulness += minutesPerSecond * 2 ;
        }
        else
        {
            this.state = VisitorState.WANNA_EAT;
            return;
        }
        if(playfulness > 50 && hunger < 50)
        {
            this.state = VisitorState.WANNA_PLAY;
            return;
        }
        if(stayingTime == 0 )
        {
            state = VisitorState.WANNA_LEAVE;
            return;
        }
        if (this.currentActivityLength == 0) {
            happiness-= minutesPerSecond;
        }
        else {
            currentActivityLength-= minutesPerSecond;
        }
        if(currentActivityLength <= 0)
        {
            currentActivityLength = 0;
        }
        return;
    }

    public int getCurrentActivityLength() {
        return currentActivityLength;
    }

    public int getHappiness() {
        return happiness;
    }

    public VisitorState getState() {
        return state;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public int getHunger() {
        return hunger;
    }

    public int getPlayfulness() {
        return playfulness;
    }

    public void setStayingTime(int stayingTime) {
        this.stayingTime = stayingTime;
    }

    public int getStayingTime() {
        return stayingTime;
    }

    public ArrayList<Position> getPathPositionList() { return pathPosition; }


    @Override
    protected Color getColor() {
        return Color.pink;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public void setPlayfulness(int playfulness) {
        this.playfulness = playfulness;
    }
}
