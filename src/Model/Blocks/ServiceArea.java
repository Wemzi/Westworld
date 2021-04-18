package Model.Blocks;

import Model.People.Caterer;
import Model.People.Employee;
import Model.People.Visitor;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class ServiceArea extends Block implements Queueable{
    private int menuCost;
    private final ArrayDeque<Visitor> queue;
    private final ArrayDeque<Visitor> visitorsUsingThisService;
    private final ArrayList<Employee> workers;
    private final int capacity;
    private final ServiceType type;
    private int cooldownTime;
    private int buildingTime;
    private static final HashMap<ServiceType,SpriteManager> spriteManagerMap=new HashMap<>();


    public ServiceArea(ServiceType type, Position pos) {
        this.type=type;
        if(type==ServiceType.BUFFET)
        {
            this.pos=pos;
            buildingTime = 30;
            buildingCost = 100;
            upkeepCost = 10;
            popularityIncrease = 1.0;
            state = BlockState.UNDER_CONSTRUCTION;
            this.menuCost = 15;
            this.capacity = 2;
            this.size=new Position(2,1,false);
            workers = new ArrayList<>();
            queue = new ArrayDeque<>();
            visitorsUsingThisService = new ArrayDeque<>(capacity);
            this.cooldownTime=5;
        }
        else if(type==ServiceType.TOILET)
        {
            this.pos=pos;
            buildingTime = 30;
            buildingCost = 75;
            upkeepCost = 10;
            popularityIncrease = 1.0;
            state = BlockState.UNDER_CONSTRUCTION;
            this.menuCost = 3;
            this.capacity = 3;
            this.size=new Position(1,3,false);
            workers = new ArrayList<>();
            queue = new ArrayDeque<>();
            visitorsUsingThisService = new ArrayDeque<>(capacity);
            this.cooldownTime=2;
        }
        else throw new RuntimeException("Invalid type of service!");
    }

    public void addWorker(Caterer o){workers.add(o);}
    public void addVisitor(Visitor v){queue.add(v);}

    @Override
    public Color getColor() {
        return Color.blue;
    }

    public ServiceType getType() {
        return type;
    }

    public int getTicketCost() {
        return menuCost;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayDeque<Visitor> getVisitorsUsingThisService() {
        return visitorsUsingThisService;
    }

    public int getMenuCost() { return menuCost; }

    public void setMenuCost(int menuCost) { this.menuCost = menuCost; }

    public ArrayList<Employee> getWorkers() { return workers; }

    public void setCooldownTime(int cooldownTime) {this.cooldownTime = cooldownTime; }

    public int getCooldownTime() { return cooldownTime; }

    private void startService(){
        Visitor v = queue.pollFirst();
        visitorsUsingThisService.add(v);
        if(getType()==ServiceType.TOILET){
            v.startToilet();
        }else if(getType()==ServiceType.BUFFET){
            v.startEating();
        }
        currentActivityTime=getCooldownTime();
        setState(BlockState.USED);
    }

    private void serviceFinished(){
        Visitor v=visitorsUsingThisService.pollFirst();
        if(getType()==ServiceType.TOILET){
            v.finishedToilet();
        }else if(getType()==ServiceType.BUFFET){
            v.finishedEating();
        }
        setState(BlockState.FREE);
    }

    private boolean isOperable(){
        if(getType()==ServiceType.BUFFET && workers.size()==0){return false;}
        return true;
    }

    @Override
    public void roundHasPassed(int minutesPerSecond)
    {
        decreaseCurrentActivityTime(minutesPerSecond);

        //finished activity
        if(getState()==BlockState.UNDER_CONSTRUCTION && currentActivityTime==0){setState(BlockState.FREE);}
        if(getState()==BlockState.UNDER_REPAIR && currentActivityTime==0){repairFinished();}
        if(getState()==BlockState.NOT_OPERABLE && isOperable() && !needRepair()){setState(BlockState.FREE);}
        if (getState()==BlockState.USED && currentActivityTime==0){serviceFinished();}

        //need to start activity
        if(getState()==BlockState.FREE && needRepair()){setState(BlockState.NOT_OPERABLE);}
        if(getState()==BlockState.FREE && !isOperable()){setState(BlockState.NOT_OPERABLE);}

        if (getState()==BlockState.FREE && isOperable() && !needRepair() && queue.size()>0 && visitorsUsingThisService.size()<capacity){startService();}
    }

    @Override
    public String toString() {
        return "ServiceArea{" + type + " " +
                "menuCost=" + menuCost +
                ", queue=" + queue +
                ", workers=" + workers +
                ", capacity=" + capacity +
                " " + super.toString();
    }

    @Override
    public String getName()
    {
        switch(this.type)
        {
            case BUFFET:return "Buffet";
            case TOILET: return "Toilet";
            default : return "undefined";
        }
    }

    private void setupSprites(){
        if(spriteManagerMap.containsKey(type)){return;}
        switch (this.type) {
            case TOILET:
                spriteManagerMap.putIfAbsent(type,new StaticSpriteManager("graphics/toilet.png",size));
                break;
            case BUFFET:
                spriteManagerMap.putIfAbsent(type,new StaticSpriteManager("graphics/buffet.png",size));
            default:
                spriteManagerMap.putIfAbsent(type,new OneColorSpriteManager(getColor(),getSize()));break;
        }
    }

    @Override
    protected SpriteManager getSpriteManager() {
        setupSprites();
        return spriteManagerMap.get(type);
    }

    @Override
    public Queue<Visitor> getQueue() {
        return queue;
    }
}
