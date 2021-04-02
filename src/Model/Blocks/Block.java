package Model.Blocks;

import Model.Position;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class Block {
    protected static final Color DEFAULT_BACKGROUNG_COLOR=new Color(52, 177, 52);
    private static final int MAX_CONDITION=100;
    protected BlockState state;
    protected int buildingCost;
    private int buildingTime;
    protected int upkeepCost;
    protected int condition;
    public Position size; //3 blokk szeles es 2 blokk magas. Ez egy relative kicsi szam!
    //public Coord pos; //bal felso eleme hol van

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Block)) return false;

        Block block = (Block) o;

        return this.state.equals(block.state)           &&
                this.buildingCost == block.buildingCost &&
                this.buildingTime == block.buildingTime &&
                this.upkeepCost == block.upkeepCost     &&
                this.condition == block.condition       &&
                this.size == block.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.buildingTime,this.buildingCost,this.size,this.pos,this.size,this.upkeepCost,this.condition,this.popularityIncrease,this.state);
    }

    public  Position pos;
    protected double popularityIncrease;
    public Block()
    {
        state = BlockState.FREE;
        buildingCost=0;
        upkeepCost=0;
        condition=100;
        size=new Position(1,1,false);
        popularityIncrease = 0;
    }

    public Block(Position p){
        this();
        pos=p;
    }

    public Block(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, Position size, Position pos) {
        this.buildingCost = buildingCost;
        this.upkeepCost = upkeepCost;
        this.popularityIncrease = popularityIncrease;
        this.state = state;
        this.size = size;
        this.pos = pos;
        condition=MAX_CONDITION;
    }

    public Block(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        this.buildingCost = buildingCost;
        this.upkeepCost = upkeepCost;
        this.popularityIncrease = popularityIncrease;
        this.state = state;
        this.size = new Position(1,1,false);
        this.pos = new Position(0,0,true);
        condition=MAX_CONDITION;
    }

    //Methods:
    public void showInfoPanel(){}

    public Position getPos() {
        return pos;
    }

    public Position getSize(){return size;}

    public BlockState getState() {
        return state;
    }

    public int getBuildingCost() {
        return buildingCost;
    }

    public int getUpkeepCost() {
        return upkeepCost;
    }

    public int getCondition() {
        return condition;
    }

    public double getPopularityIncrease() {
        return popularityIncrease;
    }

    public void setState(BlockState state) {
        this.state = state;
    }

    public void setBuildingCost(int buildingCost) {
        this.buildingCost = buildingCost;
    }

    public void setUpkeepCost(int upkeepCost) {
        this.upkeepCost = upkeepCost;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public void setPopularityIncrease(double popularityIncrease) {
        this.popularityIncrease = popularityIncrease;
    }

    public String toString() {
        return "" +
                "state=" + state +
                ", buildingCost=" + buildingCost +
                ", upkeepCost=" + upkeepCost +
                ", condition=" + condition +
                ", size=" + size +
                ", pos=" + pos +
                ", popularityIncrease=" + popularityIncrease +
                '}';
    }

    public String getName(){
        //todo implement; Determine the type of this block and return a user-friendly string like "Ferris Wheel"
        return "Block";
    }

    abstract public Color getColor();

    public void paint(Graphics2D gr){
        gr.setColor(getColor());
        gr.fillRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
        gr.setColor(Color.BLACK);
        gr.drawRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
