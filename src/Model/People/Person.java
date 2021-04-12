package Model.People;

import Model.Blocks.Block;
import Model.Direction;
import Model.Position;
import View.MainWindow2;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

abstract public class Person {
    public static final Position personSize=new Position(MainWindow2.BOX_SIZE/2,MainWindow2.BOX_SIZE/2,true);
    private Position pos;
    private Direction direction=Direction.NONE;
    protected int currentActivityLength;
    protected Person(Position startingCoord)
    {
        pos= startingCoord;
        currentActivityLength = 0;
    }

    private void moveTo(Block to)
    {
        pos = to.getPos();
    }
    public Position getPosition() {return pos;}
    public void setPosition(Position that)
    {
        pos= that;
    }
    protected Color getColor(){return Color.white;};
    abstract protected void roundHasPassed(int minutesPerSecond);
    public boolean isBusy(){return currentActivityLength>0;}

    private static final SpriteManager manager;

    static{
        //manager = new StaticSpriteManager("graphics/visitor.png",personSize);
        List<Rectangle> rectangles= Arrays.asList(
                new Rectangle(202,0,202,291)
        );
        manager=new OnePicDynamicSpriteManager("graphics/visitor.png",personSize,rectangles,10);
    }

    public void paint(Graphics2D gr){
        gr.drawImage(manager.nextSprite(),getPosition().getX_asPixel(),getPosition().getY_asPixel(),null);
    }
}
