package Model.People;

import Model.Blocks.ServiceArea;
import Model.Position;
import View.spriteManagers.OnePicDynamicSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Étteremben dolgozó alkalmazott.
 */
public class Caterer extends Employee {
    public ServiceArea workPlace;

    /**
     * konstruktor az alkalmazottunkhoz.
     * @param startingPos kezdőpozíció
     * @param salary fizetés
     */
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

    /**
     * Kiszolgáló metódus.
     * @param v a látogató
     */
    public void serve(Visitor v )
    {
        //v.eat(workPlace);
    }

    /**
     * A statisztikáiért, állapotáért felelős metódus, melyet másodpercenként hívunk.
     * @param minutesPerSecond ennyi perc telik el egy másodperc alatt.
     */
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

    /**
     * A típusát reprezentáló sztringet ad vissza.
     * @return
     */
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
        manager=new OnePicDynamicSpriteManager("graphics/caterer.png",getPersonSize(),rectangles,10);
    }
}
