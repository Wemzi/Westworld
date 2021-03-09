package View;

/**
 * A UI-on elfoglalt képzletbeli dobozkák helye a UI Gridjében. Pl.: 5.sor 4. eleme
 * Nem azonos a Coord -al. Az a kattintott pixelt jelöli. pl.: (201,54)
 */
public class IndexPair{
    public int i;
    public int j;

    public IndexPair(int i, int j) {
        this.i = i;
        this.j = j;
    }
}
