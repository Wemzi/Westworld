package Model.Blocks;

import Model.People.Operator;
import Model.People.Visitor;
import Model.Position;
import View.spriteManagers.DynamicSpriteManager;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;


public class Game extends Block implements Queueable{
    private static final HashMap<GameType, SpriteManager> imgMap=new HashMap<>();
    private int ticketCost;
    private final ArrayBlockingQueue<Visitor> queue;
    private ArrayList<Operator> workers;
    private int capacity;
    private int cooldownTime;
    private int buildingTime;
    private int currentActivityTime;
    public GameType type;


    @Deprecated
    public Game(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int ticketCost, int capacity, Position size, Position pos, int cooldownTime) {
        super(buildingCost, upkeepCost, popularityIncrease, state, size, pos);
        this.ticketCost = ticketCost;
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.cooldownTime = cooldownTime;
        this.buildingTime = 5 * cooldownTime;
        this.workers=new ArrayList<Operator>();
        setupImage();
    }
    @Deprecated
    public Game(Position size, Position pos) {
        super(0, 0, 0, BlockState.FREE, size, pos);
        this.ticketCost = 0;
        this.capacity = 0;
        this.queue = new ArrayBlockingQueue<>(capacity);
        setupImage();
    }
    // Implemented preset types of games
    public Game(GameType type,Position pos) {
        this.type = type;
        if (type == GameType.DODGEM) {
            this.buildingCost = 300;
            this.upkeepCost = 50;
            this.popularityIncrease=1.4;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=25;
            this.capacity=20;
            this.size= new Position(2, 2,false);
            this.pos = pos;
            this.cooldownTime=5;
            this.buildingTime = 5 * cooldownTime;
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.workers=new ArrayList<Operator>();
        } else if (type == GameType.FERRISWHEEL) {
            this.buildingCost = 600;
            this.upkeepCost = 150;
            this.popularityIncrease=1.7;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=40;
            this.capacity=20;
            this.size= new Position(2, 2,false);
            this.pos = pos;
            this.cooldownTime = 3;
            this.buildingTime = 5 * cooldownTime;
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.workers=new ArrayList<Operator>();
        } else if (type == GameType.RODEO)
        {
            this.buildingCost = 270;
            this.upkeepCost = 40;
            this.popularityIncrease=1.3;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=30;
            this.capacity=3;
            this.size= new Position(1, 1,false);
            this.pos = pos;
            this.cooldownTime = 2;
            this.buildingTime = 5 * cooldownTime;
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.workers=new ArrayList<Operator>();
        } else if( type == GameType.ROLLERCOASTER) {
            this.buildingCost = 800;
            this.upkeepCost = 200;
            this.popularityIncrease=2.1;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=60;
            this.capacity=15;
            this.size= new Position(2, 3,false);
            this.pos = pos;
            this.cooldownTime = 5;
            this.buildingTime = 5 * cooldownTime;
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.workers=new ArrayList<Operator>();
        } else if(type == GameType.SHOOTINGGALLERY) {
            this.buildingCost = 200;
            this.upkeepCost = 30;
            this.popularityIncrease=1.1;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=20;
            this.capacity=5;
            this.size= new Position(1, 1,false);
            this.pos = pos;
            this.cooldownTime = 2;
            this.buildingTime = 5 * cooldownTime;
            queue = new ArrayBlockingQueue<>(this.capacity);
            this.workers=new ArrayList<Operator>();
        }
        else throw new RuntimeException("Gametype not found at creating game, or not yet implemented");

        setupImage();
    }

    @Override
    public Color getColor() {
        return Color.red;
    }

    public void addWorker(Operator o){workers.add(o);}
    public void addVisitor(Visitor v){
        if( this.getState() == BlockState.FREE ) queue.add(v);
        else throw new RuntimeException("Visitor tried to get into queue, but state of Game wasn't 'FREE' ");
    }

    public int getCooldownTime() {
        return cooldownTime;
    }

    public void run(){
        if(this.state.equals(BlockState.FREE))
        {
            queue.clear();
            this.setState(BlockState.USED);
            System.out.println("Game is running...");
            currentActivityTime = cooldownTime;
            this.setCondition(this.getCondition()-2);
        }
        else throw new RuntimeException("You can't run the game cause it's not free!");
    }

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
        else if(state.equals(BlockState.FREE) && queue.remainingCapacity()==0)
        {
            workers.get(0).operate();
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

    public int getTicketCost() {
        return ticketCost;
    }

    public int getCapacity() {
        return capacity;
    }

    public ArrayBlockingQueue<Visitor> getQueue() {
        return queue;
    }

    public ArrayList<Operator> getWorkers() {
        return workers;
    }

    public int getBuildingTime() {
        return buildingTime;
    }

    public void setTicketCost(int ticketCost) {
        this.ticketCost = ticketCost;
    }

    public void setWorkers(ArrayList<Operator> workers) {
        this.workers = workers;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCooldownTime(int cooldownTime) {
        this.cooldownTime = cooldownTime;
    }

    public void setBuildingTime(int buildingTime) {
        this.buildingTime = buildingTime;
    }


    @Override
    public String toString() {
        return "Game{" +
                "Type=" + type +
                "ticketCost=" + ticketCost +
                ", queue=" + queue +
                ", workers=" + workers +
                ", capacity=" + capacity +
                ", cooldownTime=" + cooldownTime +
                '}' + super.toString();
    }

    @Override
    public String getName()
    {
        switch(this.type)
        {
            case FERRISWHEEL: return "Ferris Wheel";
            case RODEO: return "Rodeo";
            case ROLLERCOASTER: return "Roller Coaster";
            case DODGEM: return "Dodgem";
            case SHOOTINGGALLERY: return "Shooting Gallery";
            default : return "undefined";
        }
    }


    private void setupImage(){
        if(Objects.isNull(type) || imgMap.containsKey(type)){return;}
        switch (type){
            case ROLLERCOASTER:
                List<Rectangle> rectangles= Arrays.asList(
                        new Rectangle(0,0,150,227),
                        new Rectangle(150,0,150,227),
                        new Rectangle(300,0,150,227)
                );
                imgMap.put(type,new OnePicDynamicSpriteManager("graphics/rollercoaster.png",size,rectangles,10));
                break;
            case FERRISWHEEL:
                List<String> imgPaths = Arrays.asList("graphics/ferriswheel1.png", "graphics/ferriswheel2.png");
                imgMap.put(type,new DynamicSpriteManager(imgPaths,size,5));
                break;
            case RODEO:
                imgPaths = Arrays.asList("graphics/rodeo1.png", "graphics/rodeo2.png");
                imgMap.put(type,new DynamicSpriteManager(imgPaths,size,5));
                break;
            case SHOOTINGGALLERY:
                imgPaths = Arrays.asList("graphics/shooting_1.png", "graphics/shooting_2.png");
                imgMap.put(type,new DynamicSpriteManager(imgPaths,size,5));
                break;
            default:
                imgMap.put(type,new OneColorSpriteManager(getColor(),getSize())); break;
        }
    }

    @Override
    protected SpriteManager getSpriteManager() {
        setupImage();
        return imgMap.get(type);
    }
}
