package Model.People;

import Model.Blocks.Block;
import Model.Blocks.EmployeeBase;
import Model.Blocks.Road;
import Model.Playground;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Cleaner extends Employee {
    private Road whatSheCleans;

    public Cleaner(Position startingPos, int salary)
    {
        super(startingPos,salary);
    }

    public void clean(Road b )
    {
        whatSheCleans =b;
        currentActivityLength = b.getGarbage()*100;
        System.out.println("Lets clean!");
    }

    public void roundHasPassed(int minutesPerSecond)
    {
        if(currentActivityLength>0){currentActivityLength-= minutesPerSecond;}

        if(!Objects.isNull(whatSheCleans)){
            //whatSheCleans.setGarbage(whatSheCleans.getGarbage()-minutesPerSecond);
            //System.out.println("Cleaned a little");
            if(currentActivityLength <= 0){
                whatSheCleans.setGarbage(0);
                whatSheCleans=null;
                System.out.println("All cleaned");
            }
        }

    }

    @Override
    public void findGoal(Random rnd, Playground pg) {
        goal=pg.getBlockByPosition(new Position(5,0,false));
        for(Block b :pg.getBuildedObjectList()){
            if(b instanceof EmployeeBase){
                goal=b;
            }
        }
    }

    @Override
    public void arrived(int minutesPerSecond) {
        if(goal instanceof Road){
            Road r = (Road) goal;
            r.cleaner=this;
            this.clean(r);
        }
        super.arrived(minutesPerSecond);
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
