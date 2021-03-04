package Model;

public class Repairman {
    public void repair(Game g )
    {
        this.setIsBusy(true);
        g.condition = 100;
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);
    }
}
