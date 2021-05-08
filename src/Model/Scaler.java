package Model;

/**
 * Skálázó osztály, hogy többféle monitoron is fusson a játékunk.
 */
public class Scaler {
    private int boxSize;

    /**
     * Minden box egy négyzet, így elegendő egyetlen szám megadása
     * @param boxSize - a játék legkisebb alapegységének, egy box (tile, mező) mérete pixelben
     */
    public Scaler(int boxSize) {
        this.boxSize = boxSize;
    }

    public int getBoxSize() {
        return boxSize;
    }

    /**
     * @param boxSize -  játék legkisebb alapegységének, egy box (tile, mező) mérete pixelben
     */
    public void setBoxSize(int boxSize) {
        this.boxSize = boxSize;
    }
}
