package Model.Blocks;

import Model.Blocks.Block;
import Model.Blocks.BlockState;

public class ServiceArea extends Block {
    private int menuCost;
    //private ArrayBlockingQueue<Person> queue;
    //private ArrayList<Employee> workers;
    private int capacity;

    public ServiceArea(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, int menuCost, int capacity) {
        super(buildingCost, upkeepCost, popularityIncrease, state);
        this.menuCost = menuCost;
        this.capacity = capacity;
    }

    //Methods:
    //public addWorker(Operator o){}
    //public addVisitor(Visitor v){}


    public int getTicketCost() {
        return menuCost;
    }

    public int getCapacity() {
        return capacity;
    }
}
