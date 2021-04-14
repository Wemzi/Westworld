package Model.People;

import Model.Blocks.BlockState;
import Model.Blocks.Game;
import Model.Direction;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticPicturePartManager;

import java.awt.*;
import java.util.HashMap;

public class Repairman extends Employee {

    public Repairman(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public void repair(Game g ) {
        if (g.getState().equals(BlockState.FREE)) {
            g.setState(BlockState.UNDER_REPAIR);
            g.setCondition(100);
            currentActivityLength = g.getBuildingTime() / 5;
            g.setCooldownTime(currentActivityLength);
        }
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(this.currentActivityLength==0)
        {
            // go repair smth
        }
        else
        { currentActivityLength -= minutesPerSecond; }
        return;
    }

    @Override
    protected Color getColor(){return Color.yellow;};

    //drawing
    private static final HashMap<Direction,SpriteManager> spriteManagerMap;

    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    static{
        spriteManagerMap=new HashMap<>();
        String imgPath="graphics/repairman.png";
        spriteManagerMap.put(Direction.NONE,new StaticPicturePartManager(imgPath,personSize,new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.DOWN,new StaticPicturePartManager(imgPath,personSize,new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.LEFT,new StaticPicturePartManager(imgPath,personSize,new Rectangle(0,0,202,291)));
        spriteManagerMap.put(Direction.RIGHT,new StaticPicturePartManager(imgPath,personSize,new Rectangle(404,0,202,291)));
        spriteManagerMap.put(Direction.UP,new StaticPicturePartManager(imgPath,personSize,new Rectangle(606,0,202,291)));
    }
}
