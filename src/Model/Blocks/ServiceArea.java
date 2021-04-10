package Model.Blocks;

import Model.People.Caterer;
import Model.People.Employee;
import Model.People.Visitor;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class ServiceArea extends Block implements Queueable{
    private int menuCost;
    private ArrayBlockingQueue<Visitor> queue;
    private ArrayList<Employee> workers;
    private int capacity;
    private ServiceType type;
    private int cooldownTime;
    private int buildingTime;
    private int currentActivityTime;
    private static HashMap<ServiceType,SpriteManager> spriteManagerMap=new HashMap<>();

    @Deprecated
    public ServiceArea(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int menuCost, int capacity) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.menuCost = menuCost;
        this.capacity = capacity;
        workers = new ArrayList<>();
        queue = new ArrayBlockingQueue<>(capacity);
    }

    public ServiceArea(ServiceType type, Position pos) {
        this.type=type;
        if(type==ServiceType.BUFFET)
        {
            buildingTime = 30;
            buildingCost = 100;
            upkeepCost = 10;
            popularityIncrease = 1.0;
            state = BlockState.UNDER_CONSTRUCTION;
            this.menuCost = 15;
            this.capacity = 50;
            this.size=new Position(3,1,false);
            workers = new ArrayList<>();
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.cooldownTime=10;
        }
        else if(type==ServiceType.TOILET)
        {
            buildingTime = 30;
            buildingCost = 75;
            upkeepCost = 10;
            popularityIncrease = 1.0;
            state = BlockState.UNDER_CONSTRUCTION;
            this.menuCost = 3;
            this.capacity = 25;
            this.size=new Position(1,2,false);
            workers = new ArrayList<>();
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.cooldownTime=10;
        }
        else throw new RuntimeException("Invalid type of service!");
    }

    public void addWorker(Caterer o){workers.add(o);}
    public void addVisitor(Visitor v){
        if( this.getState() == BlockState.FREE ) queue.add(v);
        else throw new RuntimeException("Visitor tried to get into queue, but state of Service Area wasn't 'FREE' ");
    }

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

    public int getMenuCost() { return menuCost; }

    public void setMenuCost(int menuCost) { this.menuCost = menuCost; }

    public ArrayBlockingQueue<Visitor> getQueue() { return queue; }

    public void setQueue(ArrayBlockingQueue<Visitor> queue) { this.queue = queue; }

    public ArrayList<Employee> getWorkers() { return workers; }

    public void setWorkers(ArrayList<Employee> workers) { this.workers = workers; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public void setCooldownTime(int cooldownTime) {this.cooldownTime = cooldownTime; }

    public int getCooldownTime() { return cooldownTime; }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(workers.size() <= 1 )
        {
            state = BlockState.NOT_OPERABLE;
            return;
        }
        if(state.equals(BlockState.UNDER_CONSTRUCTION))
        {
            buildingTime-=minutesPerSecond;
        }
        if(state.equals(BlockState.USED))
        {
            currentActivityTime-=minutesPerSecond;
        }
        else if(buildingTime == 0 && !(state.equals(BlockState.USED))) {
            state = BlockState.FREE;
        }
        if(state.equals(BlockState.UNDER_REPAIR))
        {
            currentActivityTime -= minutesPerSecond;
        }
        if(state.equals(BlockState.UNDER_REPAIR) && currentActivityTime <= 0 )
        {
            state = BlockState.FREE;
            currentActivityTime = 0;
        }
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
            default:
                spriteManagerMap.put(type,new OneColorSpriteManager(getColor(),getSize()));break;
        }
    }

    @Override
    protected SpriteManager getSpriteManager() {
        setupSprites();
        return spriteManagerMap.get(type);
    }
}
