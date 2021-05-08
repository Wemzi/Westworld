package Model.Blocks;

import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Repairman;
import Model.Playground;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class EmployeeBase extends Block {
        private static SpriteManager spriteManager=null;

        public EmployeeBase(Position p) {
            super(100,0,0,BlockState.FREE);
            this.pos=p;
        }

    /**
     * Új szerelő felvétele.
     * @param r szerelő, akit fel szeretnénk venni.
     * @param pg Az a park, ahova felvennénk.
     */
        public void hire(Repairman r ,Playground pg){
            pg.hire(r);
            r.setPosition(this.getPos());
        }

    /**
     * Új takarító felvétele.
     * @param r takarító, akit fel szeretnénk venni.
     * @param pg Az a park, ahova felvennénk.
     */
        public void hire(Cleaner r , Playground pg){
            pg.hire(r);
            r.setPosition(this.getPos());
        }

        @Override
        public Color getColor() {return Color.YELLOW;}


        @Override
        public String toString() {
            return "Employee Base" +
                    " " + super.toString();
        }

        @Override
        public String getName()
        {
            return "Employee Base";
        }

    @Override
    protected SpriteManager getSpriteManager() {
        if(Objects.isNull(spriteManager)){spriteManager=new StaticSpriteManager("graphics/employeebase.png",getSize());}
        return spriteManager;
    }
}


