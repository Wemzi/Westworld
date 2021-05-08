package Model.Blocks;

import Model.GameEngine;
import Model.Position;
import View.GameField;
import View.spriteManagers.SpriteManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public abstract class Block {
    protected static final Color DEFAULT_BACKGROUND_COLOR =new Color(52, 177, 52);
    protected static final int MAX_CONDITION=100;
    protected BlockState state;
    protected int buildingCost;
    protected int upkeepCost;
    protected int condition;
    public Position size;
    public  Position pos;
    protected double popularityIncrease;

    protected int buildingTime;
    protected int currentActivityTime;

    /**
     * Default constructor for Block class
     */
    public Block()
    {
        state = BlockState.FREE;
        buildingCost=0;
        upkeepCost=0;
        condition=100;
        size=new Position(1,1,false);
        popularityIncrease = 0;
    }

    /**
     * Contsructor for Block class
     * @param p position of block
     */
    public Block(Position p){
        this();
        pos=p;
    }

    /**
     * Constructor for block class
     * @param buildingCost cost of the building
     * @param upkeepCost daily cost of the block
     * @param popularityIncrease how much it increases the popularity
     * @param state the state of the block
     * @param size the size of the block
     * @param pos the position of the block
     */
    public Block(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state, Position size, Position pos) {
        this.buildingCost = buildingCost;
        this.upkeepCost = upkeepCost;
        this.popularityIncrease = popularityIncrease;
        this.state = state;
        this.size = size;
        this.pos = pos;
        condition=MAX_CONDITION;
    }

    /**
     * Constructor for Block class, with pre defined size and position
     * @param buildingCost cost of the building
     * @param upkeepCost daily cost of the block
     * @param popularityIncrease how much it increases the popularity
     * @param state state of the block
     */
    public Block(int buildingCost, int upkeepCost, double popularityIncrease, BlockState state) {
        this(buildingCost,upkeepCost,popularityIncrease,state,new Position(1,1,false),new Position(0,0,true));
    }

    /**
     * Building an object in the game.
     */
    public void build(){
        setupSprites();
        state=BlockState.UNDER_CONSTRUCTION;
        currentActivityTime= GameEngine.TIME_1x*5;
    }

    /**
     * Building an object instantly for dev purposes.
     */
    public void buildInstantly(){
        build();
        currentActivityTime=0;
        roundHasPassed(GameEngine.TIME_1x);
    }

    /**
     * Method of Block which is ran every second by GameEngine. This helps automatic state changing, cooldowns, etc.
     * @param minutesPerSecond
     */
    public void roundHasPassed(int minutesPerSecond){
        decreaseCurrentActivityTime(minutesPerSecond);

        //finished activity
        if(getState()==BlockState.UNDER_CONSTRUCTION && currentActivityTime==0){constructionFinished();}
        if(getState()==BlockState.UNDER_REPAIR && currentActivityTime==0){repairFinished();}
        if(getState()==BlockState.NOT_OPERABLE && !needRepair()){setState(BlockState.FREE);}

        //need to start activity
        if(getState()==BlockState.FREE && needRepair()){setState(BlockState.NOT_OPERABLE);}
    }

    /**
     * this method is called when the repair is finished, and we need to set the condition and state back to normal.
     */
    protected void repairFinished(){
        condition=MAX_CONDITION;
        setState(BlockState.FREE);
        currentActivityTime=0;
    }

    /**
     * this method is called when the building is finished, and we need to set the state back to normal.
     */
    protected void constructionFinished(){setState(BlockState.FREE);}

    protected void decreaseCurrentActivityTime(int value){
        if(value <=0){throw new IllegalArgumentException("pozitiv szam kene");}
        //if(currentActivityTime==0){throw new IllegalStateException("Nincsen folyamatban semmi. BlockState: " + getState().toString());}
        if(currentActivityTime>value){
            currentActivityTime-=value;
        }else{
            currentActivityTime=0;
        }
    }

    /**
     * Methods called on the start of the day, and end of the day by GameEngine.
     */
    public void startDay(){}
    public void endDay(){}

    /**
     * Get the position of a Block.
     * @return
     */
    public Position getPos() {
        return pos;
    }

    /**
     * get the size of the Block.
     * @return
     */
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

    public int getCurrentActivityTime() {
        return currentActivityTime;
    }

    public double getPopularityIncrease() {
        return popularityIncrease;
    }

    public void setState(BlockState state) { this.state = state; }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public boolean needRepair(){
        return (state==BlockState.NOT_OPERABLE || state==BlockState.FREE) && condition<20;
    }

    public void setCurrentActivityTime(int currentActivityTime) {
        this.currentActivityTime = currentActivityTime;
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

    /**
     * @return a string representing the block's type.
     */
    public String getName(){
        return "Block";
    }

    /**
     *
     * @param o other Object to compare to.
     * @return if the two objects are equal.
     */
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

    /**
     * generate an unique code for the Blocks.
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.buildingTime,this.buildingCost,this.size,this.pos,this.size,this.upkeepCost,this.condition,this.popularityIncrease,this.state);
    }

    /**
     * Method to get the color of the block. It gets replaced if the texture is found.
     */
    abstract public Color getColor();
    abstract protected SpriteManager getSpriteManager();

    private static BufferedImage workingPic;
    private static final HashMap<Position,BufferedImage> workingPicMap =new HashMap<>();

    static{
        try {
            workingPic=ImageIO.read(new File("graphics/work.png"));
        } catch (IOException e) {
            System.err.println("graphics/work.png not found");
        }

    }

    /**
     * Get correct image, and resize it.
     */
    private void setupSprites(){
        if(!workingPicMap.containsKey(getSize()) && !Objects.isNull(workingPic)){
            workingPicMap.put(getSize(),SpriteManager.resize(workingPic,getSize()));}
    }
    /**
     * Paint the object.
     */
    public void paint(Graphics2D gr){
        switch (state){
            default:
                gr.drawImage(getSpriteManager().nextSprite(),pos.getX_asPixel(),pos.getY_asPixel(),null);
                break;
            case UNDER_CONSTRUCTION:
            case UNDER_REPAIR:
                gr.drawImage(workingPicMap.get(getSize()),pos.getX_asPixel(),pos.getY_asPixel(),null);
                break;
            case USED:
                getSpriteManager().start();
                gr.drawImage(getSpriteManager().nextSprite(),pos.getX_asPixel(),pos.getY_asPixel(),null);
                break;
            case FREE:
                getSpriteManager().stop();
                gr.drawImage(getSpriteManager().nextSprite(),pos.getX_asPixel(),pos.getY_asPixel(),null);
                break;
            case NOT_OPERABLE:
                getSpriteManager().stop();
                gr.drawImage(getSpriteManager().nextSprite(),pos.getX_asPixel(),pos.getY_asPixel(),null);
                GameField.centerString(gr,GameField.getBlockAsRectangle(this),"Not operable");
                break;
        }
        drawBorder(gr);
    }

    /**
     * Method to draw the borders of the Object.
     */
    protected void drawBorder(Graphics2D gr){
        gr.setColor(Color.BLACK);
        gr.drawRect(pos.getX_asPixel(),pos.getY_asPixel(),size.getX_asPixel(),size.getY_asPixel());
    }
}
