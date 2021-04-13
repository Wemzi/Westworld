package Model.People;

import Model.Blocks.Game;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Operator extends Employee {
    private Game operateThis;
    public Operator(Position startingPos, int salary, Game operateThis)
    {
        super(startingPos,salary);
        this.operateThis = operateThis;
    }

    public void operate()
    {
        operateThis.run();
        currentActivityLength = operateThis.getCooldownTime();
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(currentActivityLength == 0 && operateThis.getQueue().remainingCapacity()==0)
        {
            this.operate();
        }
        else
        {
            currentActivityLength-=minutesPerSecond;
        }
        if(currentActivityLength <= 0 )
        {
            currentActivityLength = 0;
        }
        return;
    }

    public Game getWorkPlace() {
        return operateThis;
    }

    @Override
    protected Color getColor(){return Color.cyan;};

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
        manager=new OnePicDynamicSpriteManager("graphics/visitor.png",personSize,rectangles,10);
    }
}
