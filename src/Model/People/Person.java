package Model.People;

import Model.Blocks.Block;
import Model.Direction;
import Model.GameEngine;
import Model.Playground;
import Model.Position;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * Absztrakt osztály a személyeknek.
 */
abstract public class Person {
    private Position pos;
    public Direction direction=Direction.NONE;
    protected int currentActivityLength;
    public ArrayList<Position> pathPosition=new ArrayList<>();
    public boolean isMoving;
    public int pathPositionIndex;
    public Block goal;
    public String name = getRandomName();

    /**
     * Az emberek mérete.
     * @return az emberek méretét.
     */
    public static Position getPersonSize(){
        return new Position(Position.scaler.getBoxSize()/2,Position.scaler.getBoxSize()/2,true);
    }

    protected Person(Position startingCoord)
    {
        pos= startingCoord;
        currentActivityLength = 0;
    }

    /**
     * Új cél keresése.
     * @param rnd random szám
     * @param pg a park, ahol vannak
     */
    public void findGoal(Random rnd, Playground pg) {}

    /**
     * Útkeresés az új célhoz.
     * @param pg a pálya.
     */
    public void setupRoute(Playground pg){

        if(Objects.isNull(goal)) return;
        //if(this instanceof Cleaner && !(goal instanceof Road)){return;}
        if(!pg.findRoute(this, getPosition(), goal.getPos())){
            System.err.println("No available route");
            return;
        }
        pathPositionIndex = getPathPositionList().size()-1;
        isMoving = true;
    }

    /**
     * Megérkezett az ember az úticélhoz.
     * @param minutesPerSecond Ennyi perc telik el másodpercenként.
     */
    public void arrived(int minutesPerSecond){
        isMoving = false;
        pathPositionIndex = 0;
        ArrayList<Position> copy = getPathPositionList();
        getPathPositionList().removeAll(copy);
        goal=null;
    }

    /**
     * Mozgás.
     * @param minutesPerSecond Ennyi perc telik el másodpercenként.
     */
    public void move(int minutesPerSecond){
        for(int counter=minutesPerSecond*2/GameEngine.TIME_1x;counter>0 && pathPositionIndex!=-1;counter--){
            Position nextBlockPosition = getPathPositionList().get(pathPositionIndex);
            boolean isArrived =  getPathPositionList().size()  == 0 || (getPosition().getX_asPixel() == getPathPositionList().get(0).getX_asPixel() &&
                    getPosition().getY_asPixel() == getPathPositionList().get(0).getY_asPixel());
            boolean isSamePosition = getPosition().getX_asPixel() == nextBlockPosition.getX_asPixel()
                    && getPosition().getY_asPixel() == nextBlockPosition.getY_asPixel();
            boolean isDifferentPosition = getPosition().getX_asPixel() != nextBlockPosition.getX_asPixel()
                    || getPosition().getY_asPixel() != nextBlockPosition.getY_asPixel();
            if(getPathPositionList().size()  != 0 && nextBlockPosition.getX_asPixel() > getPosition().getX_asPixel()){
                direction=Direction.RIGHT;
            }else if(getPathPositionList().size()  != 0  && nextBlockPosition.getX_asPixel() < getPosition().getX_asPixel()){
                direction=Direction.LEFT;
            }else if( nextBlockPosition.getY_asPixel() > getPosition().getY_asPixel()){
                direction=Direction.DOWN;
            }else if(nextBlockPosition.getY_asPixel() < getPosition().getY_asPixel()){
                direction=Direction.UP;
            }


            if (isArrived) {
                arrived(minutesPerSecond);
            }
            if (isSamePosition) {
                pathPositionIndex--;
            }
            else if (isDifferentPosition) {
                moveTo(direction,1);
            }
        }
    }

    /**
     *
     * @return Véletlenszerű név string a names.txt-ből.
     */
    public String getRandomName()
    {
        Random rnd = new Random();
        int nameindex = Math.abs(rnd.nextInt()%18000);
        BufferedReader reader;
        String ret = "";
        try {
            reader = new BufferedReader(new FileReader(
                    "src/Model/People/names.txt"));
            String line = reader.readLine();
            for(int idx=0; idx < nameindex;idx++) {
                line = reader.readLine();
            }
            ret = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Mozgás egy adott irányba,
     * @param d irány
     * @param pixel ennyi pixelt
     */
    public void moveTo(Direction d,int pixel )
    {
        if(pixel<=0){throw new IllegalArgumentException("@param pixel must be >0");}
        switch (d){
            case RIGHT:
                setPosition(new Position(pos.getX_asPixel() + pixel,pos.getY_asPixel(), true));
                break;
            case LEFT:
                setPosition(new Position(pos.getX_asPixel() - pixel,pos.getY_asPixel(), true));
                break;
            case DOWN:
                setPosition(new Position(pos.getX_asPixel(), pos.getY_asPixel() + pixel, true));
                break;
            case UP:
                setPosition(new Position(pos.getX_asPixel(), pos.getY_asPixel() - pixel, true));
                break;
            case NONE: break;
        }
        direction=d;
    }
    public Position getPosition() {return pos;}
    public ArrayList<Position> getPathPositionList() { return pathPosition; }
    public void setPosition(Position that)
    {
        pos= that;
    }
    protected Color getColor(){return Color.white;};
    abstract protected void roundHasPassed(int minutesPerSecond);
    public abstract String getPersonClass();
    public boolean isBusy(){return currentActivityLength>0;}

    public abstract SpriteManager getSpriteManager();

    public void paint(Graphics2D gr){
        gr.drawImage(getSpriteManager().nextSprite(),getPosition().getX_asPixel(),getPosition().getY_asPixel(),null);
    }
}
