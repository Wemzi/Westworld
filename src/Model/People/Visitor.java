package Model.People;

import Model.Blocks.*;
import Model.Direction;
import Model.Playground;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticPicturePartManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Visitor extends Person {
    private int happiness;
    private int hunger;
    private int playfulness;
    private int stayingTime;
    private int credit;
    private VisitorState state;


    // TODO: cyclical waiting at a game for example
    public Visitor(Position startingPos) {
        super(startingPos);
        Random rnd = new Random();
        name = getRandomName();
        happiness = Math.abs(rnd.nextInt() % 100);
        hunger = Math.abs(rnd.nextInt() % 100);
        playfulness = 50;
        stayingTime = Math.abs(rnd.nextInt() % 300) + 200;
        isMoving = false;
        pathPositionIndex = 0;
        credit = 0;
        state = VisitorState.DOESNT_KNOW;
    }

    public void startPlaying() {
        changePlayfulness(-60);
        happiness += 20;
        hunger += 15;
        //currentActivityLength = that.getCooldownTime();
        direction=Direction.NONE;
        state = VisitorState.DOING_SOMETHING;
    }

    public void finishedPlaying() {
        state=VisitorState.DOESNT_KNOW;
        currentActivityLength=0;
    }

    public void startEating(){
        state=VisitorState.EATING;
        direction=Direction.NONE;
        credit += 3;
    }
    public void finishedEating(){
        hunger = 0;
        happiness += 5;
        playfulness += 50;
        this.state = VisitorState.WANNA_TOILET;
        currentActivityLength=0;

    }
    public void startToilet(){
        state=VisitorState.DOING_SOMETHING;
        direction=Direction.NONE;
        credit += 1;
    }
    public void finishedToilet(){
        currentActivityLength = 0;
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

            System.out.println(name + " játszani megy!");
        }
        else if ( getState().equals(VisitorState.WANNA_EAT) ) {
            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
            ArrayList<ServiceArea> BuffetList = new ArrayList<>();
            for (ServiceArea svarea : SvList) {
                if (svarea.getType().equals(ServiceType.BUFFET) && svarea.getState().equals(BlockState.FREE)) {
                    BuffetList.add(svarea);
                }
            }
            if(BuffetList.size() > 0 ) goal = BuffetList.get(Math.abs(rnd.nextInt()%BuffetList.size()));
            //System.out.println(v.getPathPositionList());
            //System.out.println("Visitor enni megy! " + v.getPathPositionList().size());
        }
        else if (getState() == VisitorState.WANNA_TOILET) {
            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
            ArrayList<ServiceArea> ToiletList = new ArrayList<>();
                for (ServiceArea svarea : SvList) {
                    if (svarea.getType().equals(ServiceType.TOILET)&& svarea.getState().equals(BlockState.FREE)) {
                        ToiletList.add(svarea);
                    }
                }
            if(ToiletList.size() > 0 ) goal = ToiletList.get(Math.abs(rnd.nextInt()%ToiletList.size()));
        }
        else if(getState() == VisitorState.WANNA_LEAVE)
        {
            java.util.List<Block> BuildedObjects = pg.getBuildedObjectList();
            for(Block b : BuildedObjects)
            {
                if(b instanceof Road && ((Road) b).isEntrance())
                {
                    goal = b;
                }
            }
        }
    }


    @Override
    public void arrived(int minutesPerSecond){
        System.out.println(name + " megérkezett!");
        isMoving = false;
        pathPositionIndex = 0;
        ArrayList<Position> copy = getPathPositionList();
        getPathPositionList().removeAll(copy);

        if(Objects.isNull(goal) || !(goal instanceof Queueable)){return;}
        ((Queueable) goal).addVisitor(this);
        state=VisitorState.WAITING_IN_QUEUE;

        /*
        if(getState().equals(VisitorState.WANNA_TOILET) &&  goal != null){
            System.out.println("kaksizna!");}
        else if(getState().equals(VisitorState.WANNA_PLAY) && goal != null) {
            System.out.println("játékra vár!");
        }
        else if(getState().equals(VisitorState.WANNA_EAT) && goal != null){
            System.out.println("evett!");
        }*/
        goal=null;

    }

    private void decreaseCurrentActivityLength(int value){
        if(value <=0){throw new IllegalArgumentException("pozitiv szam kene");}
        if(currentActivityLength>value){
            currentActivityLength-=value;
        }else{
            currentActivityLength=0;
        }
    }


    @Override
    public void roundHasPassed(int minutesPerSecond) {
        System.out.println(toString());
        this.hunger += minutesPerSecond/5;
        this.stayingTime -= minutesPerSecond;
        if(!isMoving){decreaseCurrentActivityLength(minutesPerSecond); }
        if(stayingTime < 0) {
            state = VisitorState.WANNA_LEAVE;
        }
        if(state != VisitorState.DOESNT_KNOW){ return;}
        else if (hunger < 50 & state != VisitorState.WANNA_LEAVE) {
            this.playfulness += minutesPerSecond * 2 ;
        }
        else if(hunger > 50 && state == VisitorState.DOESNT_KNOW && state != VisitorState.WANNA_LEAVE)
        {
            this.state = VisitorState.WANNA_EAT;
            return;
        }
        if(playfulness > 50 && hunger < 50 && state == VisitorState.DOESNT_KNOW & state != VisitorState.WANNA_LEAVE) {
            this.state = VisitorState.WANNA_PLAY;
            return;
        }
        if (this.currentActivityLength == 0 && state == VisitorState. DOESNT_KNOW)
            {
            happiness-= minutesPerSecond;
        }
        if(currentActivityLength == 0)
        {
            state=VisitorState.DOESNT_KNOW;
        }
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

    public String getName() {
        return name;
    }

    @Override
    public String getPersonClass() {
        return "Visitor";
    }

    @Override
    protected Color getColor() {
        return Color.pink;
    }

    public void setHunger(int hunger) {
        this.hunger = hunger;
    }

    public int getCredit()  { return this.credit; }

    public void setCredit(int credit) { this.credit = credit; }

    public void changePlayfulness(int value) {
        if(playfulness<0 && playfulness+value<0){
            playfulness=0;
        }else{
            this.playfulness += value;
        }
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
    private static final HashMap<Direction,SpriteManager> spriteManagerMap;

    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    static{
        spriteManagerMap=new HashMap<>();
        String imgPath="graphics/visitor.png";
        spriteManagerMap.put(Direction.NONE,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.DOWN,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.LEFT,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(0,0,202,291)));
        spriteManagerMap.put(Direction.RIGHT,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(404,0,202,291)));
        spriteManagerMap.put(Direction.UP,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(606,0,202,291)));
    }
}
