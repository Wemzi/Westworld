package Model;

public class Repairman extends Employee {

    public Repairman(Coord startingCoord,Block startingBlock,int salary)
    {
        super(startingCoord,startingBlock,salary);
    }

    public void repair(Game g )
    {
        this.setIsBusy(true);
        //g.condition = 100;
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);
    }
}
