package Model.People;

import Model.Direction;
import Model.Position;
import View.MainWindow2;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.ArrayList;

abstract public class Person {
    public static final Position personSize=new Position(MainWindow2.BOX_SIZE/2,MainWindow2.BOX_SIZE/2,true);
    private Position pos;
    public Direction direction=Direction.NONE;
    protected int currentActivityLength;
    private ArrayList<Position> pathPosition=new ArrayList<>();
    public boolean isMoving;
    public int pathPositionIndex;


    protected Person(Position startingCoord)
    {
        pos= startingCoord;
        currentActivityLength = 0;
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
    public boolean isBusy(){return currentActivityLength!=0;}

    public abstract SpriteManager getSpriteManager();

    public void paint(Graphics2D gr){
        gr.drawImage(getSpriteManager().nextSprite(),getPosition().getX_asPixel(),getPosition().getY_asPixel(),null);
    }
}
