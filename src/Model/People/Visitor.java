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
        happiness = Math.abs(rnd.nextInt() % 100);
        hunger = Math.abs(rnd.nextInt() % 100);
        playfulness = 50;
        stayingTime = Math.abs(rnd.nextInt() % 500);
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
        state = VisitorState.DOESNT_KNOW;
    }

    public void eat(ServiceArea where) {
        hunger = 0;
        happiness += 5;
        playfulness += 50;
        currentActivityLength = where.getCooldownTime();
        this.state = VisitorState.WANNA_TOILET;
        System.out.println("Visitor evett, következő state: " + this.state);
        System.out.println(this.isBusy());
    }

    public void toilet(ServiceArea where) {
        currentActivityLength = where.getCooldownTime();
        state = VisitorState.DOESNT_KNOW;
    }

    public Road throwGarbage(Road there) {
        there.setGarbage(there.getGarbage() + 2);
        return there;
    }

    public void roundHasPassed(int minutesPerSecond) {

        this.hunger += minutesPerSecond/5;
        this.stayingTime -= minutesPerSecond;
        currentActivityLength-= minutesPerSecond;
        if(state != VisitorState.DOESNT_KNOW)
        {
            return;
        }
        if(state.equals(VisitorState.WANNA_LEAVE)) {
            System.out.println("el akarok menni!");
        }
        else if (hunger < 50) {
            this.playfulness += minutesPerSecond * 2 ;
        }
        else if(hunger > 50 && state == VisitorState.DOESNT_KNOW)
        {
            this.state = VisitorState.WANNA_EAT;
            return;
        }
        if(playfulness > 50 && hunger < 50 && state == VisitorState.DOESNT_KNOW) {
            this.state = VisitorState.WANNA_PLAY;
            return;
        }
        /*
        if(stayingTime < 0 && state == VisitorState.DOESNT_KNOW )
        {
            state = VisitorState.WANNA_LEAVE;
            return;
        }
        */

        if (this.currentActivityLength == 0 && state == VisitorState. DOESNT_KNOW)
            {
            happiness-= minutesPerSecond;
        }
        if(currentActivityLength <= 0)
        {
            currentActivityLength = 0;
            state=VisitorState.DOESNT_KNOW;
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


    @Override
    public String toString() {
        return "Visitor{" +
                "currentActivityLength=" + currentActivityLength +
                ", pathPosition=" + pathPosition +
                ", isMoving=" + isMoving +
                ", pathPositionIndex=" + pathPositionIndex +
                ", happiness=" + happiness +
                ", hunger=" + hunger +
                ", playfulness=" + playfulness +
                ", stayingTime=" + stayingTime +
                ", state=" + state +
                '}';
    }
}
