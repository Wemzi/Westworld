package Model.People;

import Model.Blocks.Block;
import Model.Blocks.EmployeeBase;
import Model.Blocks.Road;
import Model.Direction;
import Model.Playground;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticPicturePartManager;

import java.awt.*;
import java.util.HashMap;
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
    private static final HashMap<Direction,SpriteManager> spriteManagerMap;

    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    static{
        spriteManagerMap=new HashMap<>();
        String imgPath="graphics/cleaner.png";
        spriteManagerMap.put(Direction.NONE,new StaticPicturePartManager(imgPath,personSize,new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.DOWN,new StaticPicturePartManager(imgPath,personSize,new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.LEFT,new StaticPicturePartManager(imgPath,personSize,new Rectangle(0,0,202,291)));
        spriteManagerMap.put(Direction.RIGHT,new StaticPicturePartManager(imgPath,personSize,new Rectangle(404,0,202,291)));
        spriteManagerMap.put(Direction.UP,new StaticPicturePartManager(imgPath,personSize,new Rectangle(606,0,202,291)));
    }
}
