package Model.People;

import Model.Blocks.BlockState;
import Model.Blocks.Game;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

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
    private static final SpriteManager manager;

    @Override
    public SpriteManager getSpriteManager() {
        return manager;
    }

    static{
        //manager = new StaticSpriteManager("graphics/visitor.png",personSize);
        List<Rectangle> rectangles= Arrays.asList(
                new Rectangle(202,0,202,291)
        );
        manager=new OnePicDynamicSpriteManager("graphics/repairman.png",personSize,rectangles,10);
    }
}
