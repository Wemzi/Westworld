package Model;

public class Cleaner {
    public clean(Block b )
    {
        this.setIsBusy(true);
        b.garbage = 0;
        // TODO: this should be handled in the playground?
        this.setIsBusy(false);
    }
}
