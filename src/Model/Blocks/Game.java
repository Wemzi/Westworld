package Model.Blocks;

import Model.CountDown;
import Model.People.Employee;
import Model.People.Operator;
import Model.People.Visitor;
import Model.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;



public class Game extends Block {
    private int ticketCost;
    private ArrayBlockingQueue<Visitor> queue;
    private ArrayList<Employee> workers;
    private int capacity;
    private int cooldownTime; // TODO: Building time should be 5 times cooldowntime
    public GameType type;


    public Game(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int ticketCost, int capacity, Position size, Position pos, int cooldownTime) {
        super(buildingCost, upkeepCost, popularityIncrease, state, size, pos);
        this.ticketCost = ticketCost;
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.cooldownTime = cooldownTime;
    }

    public Game(Position size, Position pos) {
        super(0, 0, 0, BlockState.FREE, size, pos);
        this.ticketCost = 0;
        this.capacity = 0;
    }
    // TODO:: change cooldowntimes to minutes (/60)
    // Implemented preset types of games
    public Game(GameType type,Position pos) {
        Game ret;
        this.type = type;
        if (type == GameType.DODGEM) {
            this.buildingCost = 300;
            this.upkeepCost = 50;
            this.popularityIncrease=1.4;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=25;
            this.capacity=20;
            this.size= new Position(2, 2);
            this.pos = pos;
            this.cooldownTime=120;
        } else if (type == GameType.FERRISWHEEL) {
            this.buildingCost = 600;
            this.upkeepCost = 150;
            this.popularityIncrease=1.7;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=40;
            this.capacity=20;
            this.size= new Position(2, 3);
            this.pos = pos;
            this.cooldownTime = 75;
        } else if (type == GameType.RODEO)
        {
            this.buildingCost = 270;
            this.upkeepCost = 40;
            this.popularityIncrease=1.3;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=30;
            this.capacity=3;
            this.size= new Position(1, 1);
            this.pos = pos;
            this.cooldownTime = 90;

        } else if( type == GameType.ROLLERCOASTER) {
            this.buildingCost = 800;
            this.upkeepCost = 200;
            this.popularityIncrease=2.1;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=60;
            this.capacity=15;
            this.size= new Position(4, 2);
            this.pos = pos;
            this.cooldownTime = 120;
        } else if(type == GameType.SHOOTINGGALLERY) {
            this.buildingCost = 200;
            this.upkeepCost = 30;
            this.popularityIncrease=1.1;
            this.state=BlockState.UNDER_CONSTRUCTION;
            this.ticketCost=20;
            this.capacity=5;
            this.size= new Position(1, 1);
            this.pos = pos;
            this.cooldownTime = 120;
        }
        else throw new RuntimeException("Gametype not found, or not yet implemented");
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
    public void run(){
        queue.clear();
        this.setState(BlockState.USED);
        System.out.println("Game is running...");

        this.setCondition(this.getCondition()-2);
        this.setState(BlockState.FREE);
    }
    // TODO: create a "RoundHasPassed" method that checks condition, state, queue, and if everything is okay, then run the game
    // TODO: also do it when a block has been built, do some sort of countdown ( -- a variable )

    public int getTicketCost() {
        return ticketCost;
    }

    public int getCapacity() {
        return capacity;
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
}
