package Model.People;

import Model.Blocks.Block;
import Model.Position;

abstract public class Employee extends Person {
    private int salary;
    protected Employee(Position startingPos ,int salary)
    {
        super(startingPos);
        this.salary = salary;
    }
    public int getSalary(){return salary;}
}
