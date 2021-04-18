package Model.Blocks;

import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Repairman;
import Model.Playground;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class EmployeeBase extends Block {
        private ArrayList<Employee> workers; //todo ez mire van?
        private static SpriteManager spriteManager=null;

        public EmployeeBase(Position p) {
            super(100,0,0,BlockState.FREE);
            this.workers = new ArrayList<Employee>();
            this.pos=p;
        }

        public void addWorker(Employee o){workers.add(o); }

        public void hire(Repairman r ,Playground pg){
            pg.hire(r);
            addWorker(r);
            r.setPosition(this.getPos());
        }

        public void hire(Cleaner r , Playground pg){
            pg.hire(r);
            addWorker(r);
            r.setPosition(this.getPos());
        }

        @Override
        public Color getColor() {return Color.YELLOW;}

        public ArrayList<Employee> getWorkers() { return workers; }

        public void setWorkers(ArrayList<Employee> workers) { this.workers = workers; }


        @Override
        public String toString() {
            return "Employee Base" +
                    ", workers=" + workers +
                    " " + super.toString();
        }

        @Override
        public String getName()
        {
            return "Employee Base";
        }

    @Override
    protected SpriteManager getSpriteManager() {
        if(Objects.isNull(spriteManager)){spriteManager=new OneColorSpriteManager(getColor(),getSize());}
        return spriteManager;
    }
}


