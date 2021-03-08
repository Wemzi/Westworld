package Model.Blocks;

import Model.Blocks.Block;
import Model.Blocks.BlockState;

public class Game extends Block {
    private int ticketCost;
    //private ArrayBlockingQueue<Person> queue;
    //private ArrayList<Employee> workers;
    private int capacity;

    public Game(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int ticketCost, int capacity) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.ticketCost = ticketCost;
        this.capacity = capacity;
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
