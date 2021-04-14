package Model.People;

import Model.Blocks.Game;
import Model.Blocks.Road;
import Model.Blocks.ServiceArea;
import Model.Blocks.ServiceType;
import Model.Direction;
import Model.Playground;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticPicturePartManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Visitor extends Person {
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
        System.out.println("state = wanna toilet kovi");
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

    @Override
    public void findGoal(Random rnd, Playground pg) {
        if(isMoving){return;}
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

        roundHasPassed(minutesPerSecond);

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
    private static final HashMap<Direction,SpriteManager> spriteManagerMap;

    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    static{
        spriteManagerMap=new HashMap<>();
        String imgPath="graphics/visitor.png";
        spriteManagerMap.put(Direction.NONE,new StaticPicturePartManager(imgPath,personSize,new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.DOWN,new StaticPicturePartManager(imgPath,personSize,new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.LEFT,new StaticPicturePartManager(imgPath,personSize,new Rectangle(0,0,202,291)));
        spriteManagerMap.put(Direction.RIGHT,new StaticPicturePartManager(imgPath,personSize,new Rectangle(404,0,202,291)));
        spriteManagerMap.put(Direction.UP,new StaticPicturePartManager(imgPath,personSize,new Rectangle(606,0,202,291)));
    }
}
