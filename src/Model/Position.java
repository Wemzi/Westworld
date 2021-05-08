package Model;

import java.util.Objects;

/**
 * A UI-on elfoglalt képzletbeli dobozkák helye a UI Gridjében. Pl.: 5.sor 4. eleme
 * Nem azonos a Coord -al. Az a kattintott pixelt jelöli. pl.: (201,54)
 */
public final class Position {
    private int x;
    private int y;
    private int boxSize;
    public static Scaler scaler = new Scaler(40);

    /**
     *
     * @deprecated Add meg 3. parméterben, hogy pixelben méred-e
     */
    @Deprecated
    public Position(int x, int y) {
        this(x, y,false);
    }

    public Position(int x, int y, boolean in_pixel) {
        if(in_pixel){
            this.x = x;
            this.y = y;
        }else{
            this.x = indexToPixel(x);
            this.y = indexToPixel(y);
        }
        boxSize= scaler.getBoxSize();
    }

    /**
     *
     * @param x idex of rows
     * @param y index of cols
     * @apiNote Measured in indices and not in pixels
     */
    public Position(double x, double y){
        boxSize= scaler.getBoxSize();
        this.x= (int) (boxSize*x);
        this.y= (int) (boxSize*y);
    }

    public int getX_asIndex() {
        return pixelToIndex(x);
    }

    public int getX_asPixel() {
        return x;
    }

    public int getY_asIndex() {
        return pixelToIndex(y);
    }

    public int getY_asPixel() {
        return y;
    }

    public static Position useMagicGravity(Position from){
        return new Position(indexToPixel(pixelToIndex(from.x)) , indexToPixel(pixelToIndex(from.y)),true);
    }

    //Conversions
    public static int pixelToIndex(int coord){return coord/ scaler.getBoxSize();}
    public static int indexToPixel(int index){return index*scaler.getBoxSize();}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + getX_asIndex() +
                ", y=" + getY_asIndex() +
                '}';
    }
}
