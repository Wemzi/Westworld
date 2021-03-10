package Model.People;

import Model.Blocks.Block;
import Model.Coord;

abstract public class Employee extends Person {
    private Boolean isBusy;
    private int salary;
    protected Employee(Coord startingCoord, Block startingBlock, int salary)
    {
        super(startingCoord,startingBlock);
        this.salary = salary;
        isBusy = false;

    }
    public int getSalary(){return salary;}
    public void setIsBusy(boolean value) {isBusy = value;}
}
