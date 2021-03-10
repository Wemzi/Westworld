package Model.Blocks;

import Model.Coord;
import View.IndexPair;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import Model.People.*;

import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;



public class Game extends Block {
    private final int ticketCost;
    private ArrayBlockingQueue<Visitor> queue;
    private ArrayList<Employee> workers;
    private final int capacity;
    private Time cooldownTime;

    public Game(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int ticketCost, int capacity, IndexPair size, Coord pos, Time cooldownTime) {
        super(buildingCost, upkeepCost, popularityIncrease, state, size, pos);
        this.ticketCost = ticketCost;
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.cooldownTime = cooldownTime;
    }

    public Game(IndexPair size, Coord pos) {
        super(0, 0, 0, BlockState.FREE, size, pos);
        this.ticketCost = 0;
        this.capacity = 0;
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
        // TODO: Start Cooldown?
        System.out.println("Game is running...");
        this.setCondition(this.getCondition()-2);
        this.setState(BlockState.FREE);
    }


    public int getTicketCost() {
        return ticketCost;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public String toString() {
        return "Game{" +
                "ticketCost=" + ticketCost +
                ", queue=" + queue +
                ", workers=" + workers +
                ", capacity=" + capacity +
                ", cooldownTime=" + cooldownTime +
                '}' + super.toString();
    }
}
