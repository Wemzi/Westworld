package Model.People;

import Model.Blocks.*;
import Model.Playground;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
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
        System.out.println("state = wanna toilet kovi");
        this.state = VisitorState.WANNA_TOILET;
        System.out.println("Visitor evett, következő state: " + this.state);
    }

    public void toilet(ServiceArea where) {
        currentActivityLength = where.getCooldownTime();
        state = VisitorState.WANNA_PLAY;
    }

    public Road throwGarbage(Road there) {
        there.setGarbage(there.getGarbage() + 2);
        return there;
    }

    @Override
    void arrived(){
        isMoving = false;
        pathPositionIndex = 0;
        ArrayList<Position> copy = getPathPositionList();
        getPathPositionList().removeAll(copy);

        if(getState().equals(VisitorState.WANNA_TOILET) &&  goal != null)
        {toilet((ServiceArea) goal);}
        else if(getState().equals(VisitorState.WANNA_PLAY) && goal != null)
        {
            playGame((Game) goal);
            ((Game) goal).addVisitor(this);
        }

        else if(getState().equals(VisitorState.WANNA_EAT) && goal != null)
        {   eat( (ServiceArea) goal);
            ((ServiceArea) goal).addVisitor(this);
        }

    }

    public void findGoal(Playground pg){
        Position wheretogo = null;
        Block interactwithme = null;

        if (!isMoving && getState().equals(VisitorState.WANNA_PLAY)) {
            ArrayList<Game> GameList = pg.getBuildedGameList();
            if (GameList.size() == 0) return;

            Random rnd =new Random();
            interactwithme = GameList.get(Math.abs((rnd.nextInt())) % GameList.size());
            wheretogo = interactwithme.getPos();

            pg.findRoute(this, getPosition(), wheretogo);
            pathPositionIndex = getPathPositionList().size()-1;
            isMoving = true;
            //System.out.println("Visitor játszani megy!");
        }
        else if (!isMoving && getState().equals(VisitorState.WANNA_EAT)) {
            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
            if (SvList.size() == 0) return;
            for (ServiceArea svarea : SvList) {
                if (svarea.getType().equals(ServiceType.BUFFET)) {
                    wheretogo = svarea.getPos();
                    interactwithme = svarea;
                    break;
                }
            }
            if (wheretogo == null) return;
            pg.findRoute(this, getPosition(), wheretogo);
            pathPositionIndex = getPathPositionList().size() - 1;
            isMoving = true;
            //System.out.println(v.getPathPositionList());
            //System.out.println("Visitor enni megy! " + v.getPathPositionList().size());
        }
        else if (!isMoving && getState() == VisitorState.WANNA_TOILET) {
            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
            if (SvList.size() == 0) return;
            for(ServiceArea svarea : SvList)
            {
                if(svarea.getType().equals(ServiceType.TOILET)) {
                    wheretogo = svarea.getPos();
                    interactwithme = svarea;
                    break;
                }
            }
            if(wheretogo == null) return;
            pg.findRoute(this, getPosition(), wheretogo);
            pathPositionIndex = getPathPositionList().size()-1;
            isMoving = true;
            //System.out.println(v.getPathPositionList());
            //System.out.println("Visitor WC-re megy!");
        }
    }

    @Override
    protected void roundHasPassed(int minutesPerSecond) {
        if(state.equals(VisitorState.WANNA_LEAVE))
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
        if(playfulness > 50 && hunger < 50) {
            this.state = VisitorState.WANNA_PLAY;
            return;
        }
        if(stayingTime == 0 )
        {
            state = VisitorState.WANNA_LEAVE;
            return;
        }
        else {
            this.stayingTime -= minutesPerSecond;
        }
        if (this.currentActivityLength == 0)
            {
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
