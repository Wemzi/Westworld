package Model;

public class Caterer {
    SericeArea workPlace;
    public serve(Visitor v )
    {
        this.setIsBusy(true);
        v.eat(workPlace)
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);
    }
}
