package Model.People;

import Model.Blocks.Block;
import Model.Blocks.Road;
import Model.Direction;
import Model.Playground;
import Model.Position;
import View.MainWindow2;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

abstract public class Person {
    public static final Position personSize=new Position(MainWindow2.BOX_SIZE/2,MainWindow2.BOX_SIZE/2,true);
    private Position pos;
    public Direction direction=Direction.NONE;
    protected int currentActivityLength;

    public ArrayList<Position> pathPosition=new ArrayList<>();
    public boolean isMoving;
    public int pathPositionIndex;
    public Block goal;


    protected Person(Position startingCoord)
    {
        pos= startingCoord;
        currentActivityLength = 0;
    }

    public void findGoal(Random rnd, Playground pg) {}

    public void setupRoute(Playground pg){

        if(Objects.isNull(goal)) return;
        if(this instanceof Cleaner && !(goal instanceof Road)){return;}
        if(!pg.findRoute(this, getPosition(), goal.getPos())){
            System.err.println("No available route");
        }
        pathPositionIndex = getPathPositionList().size()-1;
        isMoving = true;
    }

    public void arrived(int minutesPerSecond){
        isMoving = false;
        pathPositionIndex = 0;
        ArrayList<Position> copy = getPathPositionList();
        getPathPositionList().removeAll(copy);

        //todo megerkeztunk, akkor valamit csinalni is kene
        goal=null;
        System.out.println("Person megérkezett!");
    }

    public void move(int minutesPerSecond){
            if(pathPositionIndex==-1){
                System.err.println("v.pathPositionIndex==-1"); return; // todo find out why
            }
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
                direction=Direction.UP;
            }else if(nextBlockPosition.getY_asPixel() < getPosition().getY_asPixel()){
                direction=Direction.DOWN;
            }


            if (isArrived) {
                arrived(minutesPerSecond);
            }
            if (isSamePosition) {
                pathPositionIndex--;
            }
            else if (isDifferentPosition) {
                moveTo(direction,((minutesPerSecond/3) + 1));
            }
    }

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
            case UP:
                setPosition(new Position(pos.getX_asPixel(), pos.getY_asPixel() + pixel, true));
                break;
            case DOWN:
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
    public boolean isBusy(){return currentActivityLength>0;}

    public abstract SpriteManager getSpriteManager();

    public void paint(Graphics2D gr){
        gr.drawImage(getSpriteManager().nextSprite(),getPosition().getX_asPixel(),getPosition().getY_asPixel(),null);
    }
}
