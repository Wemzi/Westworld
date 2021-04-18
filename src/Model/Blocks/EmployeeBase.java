package Model.Blocks;

import Model.People.Caterer;
import Model.People.Employee;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class EmployeeBase extends Block {
        private ArrayList<Employee> workers;
        private int cooldownTime;
        private static SpriteManager spriteManager=null;

        public EmployeeBase(Position p) {
            super(100,0,0,BlockState.FREE);
            this.workers = new ArrayList<Employee>();
            this.pos=p;
        }

        public void addWorker(Caterer o){workers.add(o); }

        @Override
        public Color getColor() {
            return Color.YELLOW;}

        public ArrayList<Employee> getWorkers() { return workers; }

        public void setWorkers(ArrayList<Employee> workers) { this.workers = workers; }

        public void setCooldownTime(int cooldownTime) {this.cooldownTime = cooldownTime; }

        public int getCooldownTime() { return cooldownTime; }

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


