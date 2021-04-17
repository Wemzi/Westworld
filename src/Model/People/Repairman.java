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
import java.util.Objects;
import java.util.Random;

public class Repairman extends Employee {

    public Repairman(Position startingPos, int salary) {
        super(startingPos, salary);
    }

    public void repair(Game g) {
        if (g.getState() == BlockState.FREE) {
            g.setState(BlockState.UNDER_REPAIR);
            g.setCondition(100);
            currentActivityLength = g.getBuildingCost() / 10;
            g.setCooldownTime(currentActivityLength);
        }
    }

    public void roundHasPassed(int minutesPerSecond) {
        System.out.println(toString());
        if (currentActivityLength > 0) {
            currentActivityLength -= minutesPerSecond;
        }
        return;
    }


    @Override
    public void findGoal(Random rnd, Playground pg) {
        for(Block b :pg.getBuildedGameList()){
            if(b instanceof Game && ((Game) b).getCondition() < 0 && ! isBusy())
            {
                goal = b;
                System.out.println("megyek javitani");
                return;
            }
            else if(b instanceof EmployeeBase && !isBusy()){
                goal=b;
                System.out.println("megvan az employeebase");
            }
        }
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
