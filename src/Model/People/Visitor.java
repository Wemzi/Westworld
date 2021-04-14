package Model.People;

import Model.Blocks.Game;
import Model.Blocks.Road;
import Model.Blocks.ServiceArea;
import Model.Blocks.ServiceType;
import Model.Playground;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Visitor extends Person {
    private int happiness;
    private int hunger;
    private int playfulness;
    private int stayingTime;
    private VisitorState state;
    private String name;


    // TODO: cyclical waiting at a game for example
    public Visitor(Position startingPos) {
        super(startingPos);
        Random rnd = new Random();
        name = getRandomName();
        happiness = Math.abs(rnd.nextInt() % 100);
        hunger = Math.abs(rnd.nextInt() % 100);
        playfulness = 50;
        stayingTime = Math.abs(rnd.nextInt() % 500);
        isMoving = false;
        pathPositionIndex = 0;
        state = VisitorState.DOESNT_KNOW;
    }

    public void playGame(Game that) {
        playfulness -= 60;
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
    }

    public void toilet(ServiceArea where) {
        currentActivityLength = where.getCooldownTime();
        state = VisitorState.DOESNT_KNOW;
    }

    public Road throwGarbage(Road there) {
        there.setGarbage(there.getGarbage() + 2);
        return there;
    }

    @Override
    public void findGoal(Random rnd, Playground pg) {
        if(isMoving || isBusy()){return;}
        if ( getState().equals(VisitorState.WANNA_PLAY)) {
            ArrayList<Game> GameList = pg.getBuildedGameList();
            if (GameList.size() == 0) return;

            goal = GameList.get(Math.abs((rnd.nextInt())) % GameList.size());

            System.out.println("Visitor játszani megy!");
        }
        else if ( getState().equals(VisitorState.WANNA_EAT)) {
            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
            if (SvList.size() == 0) return;
            for (ServiceArea svarea : SvList) {
                if (svarea.getType().equals(ServiceType.BUFFET)) {
                    goal = svarea;
                    break;
                }
            }
            //System.out.println(v.getPathPositionList());
            //System.out.println("Visitor enni megy! " + v.getPathPositionList().size());
        }
        else if (getState() == VisitorState.WANNA_TOILET) {
            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
            if (SvList.size() == 0) return;
            for(ServiceArea svarea : SvList)
            {
                if(svarea.getType().equals(ServiceType.TOILET)) {
                    goal = svarea;
                    break;
                }
            }
            //System.out.println(v.getPathPositionList());
            //System.out.println("Visitor WC-re megy!");
        }
    }


    @Override
    public void arrived(int minutesPerSecond){
        System.out.println("Visitor megérkezett!");
        isMoving = false;
        pathPositionIndex = 0;
        ArrayList<Position> copy = getPathPositionList();
        getPathPositionList().removeAll(copy);

        if(getState().equals(VisitorState.WANNA_TOILET) &&  goal != null){
            toilet((ServiceArea) goal);
            System.out.println("kaksizott!");}
        else if(getState().equals(VisitorState.WANNA_PLAY) && goal != null) {
            playGame((Game) goal);
            System.out.println("játszott!");
        }
        else if(getState().equals(VisitorState.WANNA_EAT) && goal != null){
            eat( (ServiceArea) goal);
            System.out.println("evett!");
        }
        goal=null;

    }



    public void roundHasPassed(int minutesPerSecond) {
        System.out.println(toString());
        this.hunger += minutesPerSecond/5;
        this.stayingTime -= minutesPerSecond;
        if(!isMoving)
        {
            currentActivityLength-= minutesPerSecond;
        }
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
        if(playfulness < 0)
        {
            playfulness = 0;
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
                "neve: " + name +
                " currentActivityLength=" + currentActivityLength +
                ", isMoving=" + isMoving +
                ", pathPositionIndex=" + pathPositionIndex +
                ", happiness=" + happiness +
                ", hunger=" + hunger +
                ", playfulness=" + playfulness +
                ", stayingTime=" + stayingTime +
                ", state=" + state +
                '}';
    }


    //drawing
    private static final SpriteManager manager;

    @Override
    public SpriteManager getSpriteManager() {
        return manager;
    }

    static{
        //manager = new StaticSpriteManager("graphics/visitor.png",personSize);
        List<Rectangle> rectangles= Arrays.asList(
                new Rectangle(202,0,202,291)
        );
        manager=new OnePicDynamicSpriteManager("graphics/visitor.png",personSize,rectangles,10);
    }
}
