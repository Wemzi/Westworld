package Model.People;

import Model.Blocks.Road;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cleaner extends Employee {
    public Road whatSheCleans;

    public Cleaner(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public void clean(Road b )
    {
        whatSheCleans =b;
        currentActivityLength = 20;
        System.out.println("Clean!");
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(currentActivityLength == 0)
        {
            if(!Objects.isNull(whatSheCleans)){
                System.out.println("Cleaned");
                whatSheCleans.setGarbage(0);
                whatSheCleans=null;
            }
        }
        else
        {
            currentActivityLength-= minutesPerSecond;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.MAGENTA;};

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
        manager=new OnePicDynamicSpriteManager("graphics/cleaner.png",personSize,rectangles,10);
    }
}
