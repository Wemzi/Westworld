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

public class Repairman extends Employee {

    public Repairman(Position startingPos, int salary) {
        super(startingPos, salary);
    }

    public void repair(Game g) {
        if (g.getState() == BlockState.FREE || g.getState()==BlockState.NOT_OPERABLE) {
            g.setState(BlockState.UNDER_REPAIR);
            currentActivityLength = g.getBuildingCost() / 10;
            g.setCurrentActivityTime(currentActivityLength);
            goal = null;
        }
    }

    @Override
    public void roundHasPassed(int minutesPerSecond) {
        System.out.println(toString());
        if (currentActivityLength > 0) {
            currentActivityLength -= minutesPerSecond;
        }else{
            currentActivityLength=0;
        }
    }


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

    @Override
    public String getPersonClass() {
        return "Repairman";
    }

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

    @Override
    public SpriteManager getSpriteManager() {
        return spriteManagerMap.get(direction);
    }

    static {
        spriteManagerMap = new HashMap<>();
        String imgPath = "graphics/repairman.png";
        spriteManagerMap.put(Direction.NONE, new StaticPicturePartManager(imgPath, personSize, new Rectangle(202, 0, 202, 291)));
        spriteManagerMap.put(Direction.DOWN, new StaticPicturePartManager(imgPath, personSize, new Rectangle(202, 0, 202, 291)));
        spriteManagerMap.put(Direction.LEFT, new StaticPicturePartManager(imgPath, personSize, new Rectangle(0, 0, 202, 291)));
        spriteManagerMap.put(Direction.RIGHT, new StaticPicturePartManager(imgPath, personSize, new Rectangle(404, 0, 202, 291)));
        spriteManagerMap.put(Direction.UP, new StaticPicturePartManager(imgPath, personSize, new Rectangle(606, 0, 202, 291)));
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
