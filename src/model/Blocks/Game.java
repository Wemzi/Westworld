package Model.Blocks;

import Model.Coord;
import View.IndexPair;

import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import Model.People.*;
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
    public void addVisitor(Visitor v){queue.add(v);}
    public void run(){
        queue.clear();
    }


    public int getTicketCost() {
        return ticketCost;
    }

    public int getCapacity() {
        return capacity;
    }
}
