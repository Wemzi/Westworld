package Model;

abstract public class Person {
    private Block posBlock;
    private Coord posCoord;

    protected Person(Coord startingCoord,Block startingBlock)
    {
        posCoord = startingCoord;
        posBlock = startingBlock;

    }

    private void moveTo(Block to)
    {
        posBlock = to;
    }

    public Block getPosition() {return posBlock;}
    public Coord getCoord() {return posCoord;}
    public void setPosition(Coord that)
    {
        posCoord = that;
    }


}
