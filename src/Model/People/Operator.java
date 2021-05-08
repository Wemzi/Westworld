package Model.People;

import Model.Blocks.Game;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Operátorok, akik a játékokat üzemeltetik.
 */
public class Operator extends Employee {
    private Game operateThis;
    public Operator(Position startingPos, int salary, Game operateThis)
    {
        super(startingPos,salary);
        this.operateThis = operateThis;
    }

    /**
     * Egy játék operálása.
     */
    public void operate()
    {
        //operateThis.run();
        //currentActivityLength = operateThis.getCooldownTime();
    }

    /**
     * A tevékenységeket, statisztikákat állító metódus, mely másodpercenként fut.
     * @param minutesPerSecond ennyi perc telik le egy másodperc alatt.
     */
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

    @Override
    public String getPersonClass() {
        return "Operator";
    }

    //drawing
    private static final SpriteManager manager;

    /**
     * Get the 4 direction sprites for the class, and set them.
     * @return
     */
    @Override
    public SpriteManager getSpriteManager() {
        return manager;
    }

    static{
        //manager = new StaticSpriteManager("graphics/visitor.png",personSize);
        List<Rectangle> rectangles= Arrays.asList(
                new Rectangle(202,0,202,291)
        );
        manager=new OnePicDynamicSpriteManager("graphics/visitor.png",getPersonSize(),rectangles,10);
    }


    @Override
    public void paint(Graphics2D gr) {
        //ne rajzoljuk ki az operatorokat
    }
}
