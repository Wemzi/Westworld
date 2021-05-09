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

/**
 * Takarító. Ő szedi fel a szemetet a parkból.
 */
public class Cleaner extends Employee {
    private Road whatSheCleans;
    public String name;

    /**
     * Új takarító létrehozása.
     * @param startingPos kezdőpozíció
     * @param salary fizetés.
     */
    public Cleaner(Position startingPos, int salary)
    {
        super(startingPos,salary);
        name = getRandomName();
    }

    /**
     * Egy út feltakarítása.
     * @param b az út.
     */
    public void clean(Road b )
    {
        whatSheCleans =b;
        currentActivityLength = b.getGarbage();
        System.out.println("Lets clean!");
    }

    /**
     * A tevékenységeket, statisztikákat állító metódus, mely másodpercenként fut.
     * @param minutesPerSecond ennyi perc telik le egy másodperc alatt.
     */
    public void roundHasPassed(int minutesPerSecond)
    {
        //System.out.println(toString());
        if(currentActivityLength>0 ){
            currentActivityLength-= minutesPerSecond;
        }
        if(!Objects.isNull(whatSheCleans)){
            if(currentActivityLength <= 0){
                whatSheCleans.setGarbage(0);
                whatSheCleans.cleaner = null;
                whatSheCleans=null;
                System.out.println("All cleaned");
            }
        }
    }

    /**
     * Egy új cél keresése.
     * @param rnd random szám generátor.
     * @param pg a park, amiben ő van.
     */
    @Override
    public void findGoal(Random rnd, Playground pg) {
        if(goal != null) return;
        for(Block b :pg.getBuildedObjectList()){
            if(b instanceof Road && ((Road) b).getGarbageLevel() != Road.GarbageLevel.NONE && ! isBusy())
            {
                goal = b;
                System.out.println("KOSZOS AZ EGESZ HOBELEVANC");
                return;
            }
        }
        if((pg.getBlockByPosition(getPosition()) instanceof EmployeeBase)) return;
        for(Block b :pg.getBuildedEmployeeBases())
        {
            if(b instanceof EmployeeBase && !isBusy() && !(pg.getBlockByPosition(getPosition()) instanceof EmployeeBase))
            {
                goal = b;
                System.out.println("Megvan az employeebase");
                return;
            }
        }
    }

    /**
     * meghívódik az útkereső által, ha megérkezett a takarító.
     * @param minutesPerSecond ennyi perc telik le egy másodperc alatt.
     */
    @Override
    public void arrived(int minutesPerSecond) {
        if(goal instanceof Road){
            Road r = (Road) goal;
            r.cleaner=this;
            this.clean(r);
        }
        super.arrived(minutesPerSecond);
    }

    public Road getWhatSheCleans() {
        return whatSheCleans;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getPersonClass() {
        return "Cleaner";
    }

    @Override
    protected Color getColor(){return Color.MAGENTA;};

    //drawing
    private static final HashMap<Direction,SpriteManager> spriteManagerMap;

    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    /**
     * a négy különböző írányu sprite beállítása.
     */
    static{
        spriteManagerMap=new HashMap<>();
        String imgPath="graphics/cleaner.png";
        spriteManagerMap.put(Direction.NONE,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.DOWN,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(202,0,202,291)));
        spriteManagerMap.put(Direction.LEFT,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(0,0,202,291)));
        spriteManagerMap.put(Direction.RIGHT,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(404,0,202,291)));
        spriteManagerMap.put(Direction.UP,new StaticPicturePartManager(imgPath,getPersonSize(),new Rectangle(606,0,202,291)));
    }

    @Override
    public String toString() {
        return "Cleaner{" + "name= " +name + " " +
                "whatSheCleans=" + whatSheCleans +
                ", currentActivityLength=" + currentActivityLength +
                ", pathPosition=" + pathPosition +
                ", isMoving=" + isMoving +
                ", pathPositionIndex=" + pathPositionIndex +
                ", goal=" + goal +
                '}';
    }
}
