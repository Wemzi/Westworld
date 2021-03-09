package Model.Blocks;

import Model.Coord;
import View.IndexPair;

public class Game extends Block {
    private int ticketCost;
    //private ArrayBlockingQueue<Person> queue;
    //private ArrayList<Employee> workers;
    private int capacity;

    public Game(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int ticketCost, int capacity, IndexPair size, Coord pos) {
        super(buildingCost, upkeepCost, popularityIncrease, state, size, pos);
        this.ticketCost = ticketCost;
        this.capacity = capacity;
    }

    public Game(IndexPair size, Coord pos) {
        super(0, 0, 0, BlockState.FREE, size, pos);
        this.ticketCost = 0;
        this.capacity = 0;
    }

    //Methods:
    //public addWorker(Operator o){}
    //public addVisitor(Visitor v){}


    public int getTicketCost() {
        return ticketCost;
    }

    public int getCapacity() {
        return capacity;
    }
}
