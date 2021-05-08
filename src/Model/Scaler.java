package Model;

/**
 * Skálázó osztály, hogy többféle monitoron is fusson a játékunk.
 */
public class Scaler {
    private int boxSize;

    public Scaler(int boxSize) {
        this.boxSize = boxSize;
    }

    public int getBoxSize() {
        return boxSize;
    }

    public void setBoxSize(int boxSize) {
        this.boxSize = boxSize;
    }
}
