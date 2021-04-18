package Model.People;

import Model.Blocks.ServiceArea;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Caterer extends Employee {
    public ServiceArea workPlace;

    @Deprecated
    public Caterer(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public Caterer(Position startingPos, int salary, ServiceArea workPlace)
    {
        super(startingPos,salary);
        this.workPlace=workPlace;
    }

    public void serve(Visitor v )
    {
        //v.eat(workPlace);
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(currentActivityLength == 0)
        {
            Visitor hungryGuest = workPlace.getQueue().remove();
            this.serve(hungryGuest);
            currentActivityLength = 1;
        }
        else
        {
            currentActivityLength-=minutesPerSecond;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.gray;};

    @Override
    public String getPersonClass() {
        return "Caterer";
    }

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
        manager=new OnePicDynamicSpriteManager("graphics/caterer.png",personSize,rectangles,10);
    }
}
