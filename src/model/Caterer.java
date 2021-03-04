package Model;

public class Caterer extends Employee {
    //SericeArea workPlace;

    public Caterer(Coord startingCoord,Block startingBlock,int salary)
    {
        super(startingCoord,startingBlock,salary);
    }

    public void serve(Visitor v )
    {
        this.setIsBusy(true);
        //v.eat(workPlace)
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);
    }
}
