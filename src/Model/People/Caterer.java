package Model.People;

import Model.Blocks.*;
import Model.Position;

import java.awt.*;

public class Caterer extends Employee {
    ServiceArea workPlace;

    public Caterer(Position startingPos, int salary, ServiceArea workPlace)
    {
        super(startingPos,salary);
        this.workPlace=workPlace;
    }

    public void serve(Visitor v )
    {
        v.eat(workPlace);
    }

    public void roundHasPassed()
    {
        if(currentActivityLength == 0)
        {
            Visitor hungryGuest = workPlace.getQueue().remove();
            this.serve(hungryGuest);
            currentActivityLength = 1;
            // TODO: Money?
        }
        else
        {
            currentActivityLength--;
        }
        return;
    }

    @Override
    protected Color getColor(){return Color.gray;};
}
