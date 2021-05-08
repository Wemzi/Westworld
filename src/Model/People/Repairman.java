package Model.People;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.EmployeeBase;
import Model.Blocks.Game;
import Model.Direction;
import Model.Playground;
import Model.Position;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticPicturePartManager;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

/**
 * Szerelő osztály, ők javítják az elromlott objektumokat.
 */
public class Repairman extends Employee {

    public Repairman(Position startingPos, int salary) {
        super(startingPos, salary);
    }

    /**
     * Javítás.
     * @param g a játék, amit meg kell javítani.
     */
    public void repair(Game g) {
        if (g.getState() == BlockState.FREE || g.getState()==BlockState.NOT_OPERABLE) {
            g.setState(BlockState.UNDER_REPAIR);
            currentActivityLength = g.getBuildingCost() / 10;
            g.setCurrentActivityTime(currentActivityLength);
            goal = null;
        }
    }

    /**
     * A tevékenységeket, statisztikákat állító metódus, mely másodpercenként fut.
     * @param minutesPerSecond ennyi perc telik le egy másodperc alatt.
     */
    @Override
    public void roundHasPassed(int minutesPerSecond) {
        //System.out.println(toString());
        if (currentActivityLength > 0) {
            currentActivityLength -= minutesPerSecond;
        }else{
            currentActivityLength=0;
        }
    }

    /**
     * új cél keresése
     * @param rnd random szám
     * @param pg a park, ahol vannak
     */
    @Override
    public void findGoal(Random rnd, Playground pg) {
        for(Block b :pg.getBuildedGameList()){
            if(b instanceof Game && ((Game) b).needRepair() && ! isBusy())
            {
                goal = b;
                System.out.println("megyek javitani");
                return;
            }
        }
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
     *
     * @return sztring, ami reprezentálja a típust.
     */
    @Override
    public String getPersonClass() {
        return "Repairman";
    }

    /**
     * Megérkezett a szerelő a helyszínre.
     * @param minutesPerSecond Ennyi perc telik el másodpercenként.
     */
    @Override
    public void arrived(int minutesPerSecond) {
        if(goal instanceof Game){
            Game g = (Game) goal;
            g.repairer = this;
            this.repair(g);
        }
        super.arrived(minutesPerSecond);
    }

    @Override
    protected Color getColor() {
        return Color.yellow;
    }

    ;

    //drawing
    private static final HashMap<Direction, SpriteManager> spriteManagerMap;

    /**
     *
     * @return Beállítja a négy iránynak megfelelő spriteot, és hozzárendeli az osztályhoz.
     */
    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    static {
        spriteManagerMap = new HashMap<>();
        String imgPath = "graphics/repairman.png";
        spriteManagerMap.put(Direction.NONE, new StaticPicturePartManager(imgPath, getPersonSize(), new Rectangle(202, 0, 202, 291)));
        spriteManagerMap.put(Direction.DOWN, new StaticPicturePartManager(imgPath, getPersonSize(), new Rectangle(202, 0, 202, 291)));
        spriteManagerMap.put(Direction.LEFT, new StaticPicturePartManager(imgPath, getPersonSize(), new Rectangle(0, 0, 202, 291)));
        spriteManagerMap.put(Direction.RIGHT, new StaticPicturePartManager(imgPath, getPersonSize(), new Rectangle(404, 0, 202, 291)));
        spriteManagerMap.put(Direction.UP, new StaticPicturePartManager(imgPath, getPersonSize(), new Rectangle(606, 0, 202, 291)));
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Repairman{" +
                ", currentActivityLength=" + currentActivityLength +
                ", pathPosition=" + pathPosition +
                ", isMoving=" + isMoving +
                ", pathPositionIndex=" + pathPositionIndex +
                ", goal=" + goal +
                '}';
    }
}
