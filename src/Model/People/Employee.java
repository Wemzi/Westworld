package Model.People;

import Model.Blocks.Block;
import Model.Position;

abstract public class Employee extends Person {
    private Boolean isBusy;
    private int salary;
    protected Employee(Position startingPos ,int salary)
    {
        super(startingPos);
        this.salary = salary;
        isBusy = false;

    }
    public int getSalary(){return salary;}
    public void setIsBusy(boolean value) {isBusy = value;}
}
