package Model;

import static View.MainWindow2.BOX_SIZE;

/**
 * A UI-on elfoglalt képzletbeli dobozkák helye a UI Gridjében. Pl.: 5.sor 4. eleme
 * Nem azonos a Coord -al. Az a kattintott pixelt jelöli. pl.: (201,54)
 */
public final class Position {
    private int x;
    private int y;

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

    }

    public void setX_asIndex(int x) { this.x = x; }
    public void setY_asIndex(int y) { this.y = y; }


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
    public static int pixelToIndex(int coord){return coord/BOX_SIZE;}
    public static int indexToPixel(int index){return index*BOX_SIZE;}

    @Override
    public String toString() {
        return "{" +
                "x=" + getX_asIndex() +
                ", y=" + getY_asIndex() +
                '}';
    }
}
