import Model.Blocks.*;
import Model.GameEngine;
import Model.People.Caterer;
import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Repairman;
import Model.Playground;
import Model.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class testGame {

    private Playground pg;

    private GameEngine engine;

    @Before
    public void createGameEngine(){
        engine=new GameEngine();
    }

    @Before
    public void createPg(){pg=new Playground();}

    @Test
    public void buildBlock_game(){
        Position p=new Position(0,0,false);
        Block b=new Game(GameType.FERRISWHEEL,p);
        assertTrue(pg.buildBlock(b,p.getX_asIndex(),p.getY_asIndex()));
        assertEquals(pg.blocks[0][0],b);
    }

    @Test
    public void buildBlock_usedPlace_PG(){
        Position p=new Position(0,0,false);
        Block b1=new Game(GameType.FERRISWHEEL,p);
        Block b2=new Game(GameType.FERRISWHEEL,p);
        assertTrue(pg.buildBlock(b1,0,0));
        assertEquals(pg.blocks[0][0],b1);
        assertFalse(pg.buildBlock(b2,0,0));
    }

    @Test
    public void buildBlock_demolishFreePlace(){
        Position p=new Position(0,0,false);
        FreePlace freePl=new FreePlace(p);
        pg.demolishBlock(freePl,0,0);
        assertTrue(pg.blocks[0][0] instanceof FreePlace);
    }

    @Test
    public void isBuildable_game(){
        Position p=new Position(0,0,false);
        Block b=new Game(GameType.FERRISWHEEL,p);
        pg.buildBlock(new FreePlace(p),0,0);
        assertTrue(pg.blocks[0][0] instanceof FreePlace);
        assertFalse(pg.isBuildable(b));
    }

    @Test
    public void isBuildable_game_usedSpace(){
        Position p=new Position(0,0,false);
        Block b1=new Game(GameType.FERRISWHEEL,p);
        pg.blocks[0][0]=b1;
        Block b2=new Game(GameType.FERRISWHEEL,p);
        assertFalse(pg.isBuildable(b2));
    }

    @Test
    public void hireCaterer(){
        //init
        Position p =new Position(0,0,true);
        ServiceArea buffet=new ServiceArea(ServiceType.BUFFET,p);
        pg.buildBlock(buffet,0,0);

        //before hire
        assertEquals(0, pg.getCateres().size());
        assertEquals(0,buffet.getWorkers().size());

        //hire
        Caterer c=new Caterer(p,1000,buffet);
        pg.hire(c);

        //after hire
        assertEquals(1, pg.getCateres().size());
        assertEquals(c, pg.getCateres().get(0));
        assertEquals(1,buffet.getWorkers().size());
    }

    @Test
    public void fireCaterer(){
        //init
        Position p =new Position(0,0,true);
        ServiceArea buffet=new ServiceArea(ServiceType.BUFFET,p);
        pg.buildBlock(buffet,0,0);

        //before hire
        assertEquals(0, pg.getCateres().size());
        assertEquals(0,buffet.getWorkers().size());

        //hire
        Caterer c=new Caterer(p,1000,buffet);
        pg.hire(c);

        //after hire
        assertEquals(1, pg.getCateres().size());
        assertEquals(c, pg.getCateres().get(0));
        assertEquals(1,buffet.getWorkers().size());

        //fire
        pg.fire(c);

        //afterFire
        assertEquals(0, pg.getCateres().size());
        assertEquals(0,buffet.getWorkers().size());
    }

    @Test
    public void hireRepairman(){
        //init
        Position p =new Position(0,0,true);

        //before hire
        assertEquals(0, pg.getRepairmen().size());

        //hire
        Employee e =new Repairman(p,1000);
        pg.hire(e);

        //after hire
        assertEquals(1, pg.getRepairmen().size());
        assertEquals(e, pg.getRepairmen().get(0));
    }

    @Test
    public void hireCleaner(){
        //init
        Position p =new Position(0,0,true);

        //before hire
        assertEquals(0, pg.getCleaners().size());

        //hire
        Employee e =new Cleaner(p,1000);
        pg.hire(e);

        //after hire
        assertEquals(1, pg.getCleaners().size());
        assertEquals(e, pg.getCleaners().get(0));
    }

    @Test
    public void fireCleaner(){
        //init
        Position p =new Position(0,0,true);

        //before hire
        assertEquals(0, pg.getCleaners().size());

        //hire
        Employee e =new Cleaner(p,1000);
        pg.hire(e);

        //after hire
        assertEquals(1, pg.getCleaners().size());
        assertEquals(e, pg.getCleaners().get(0));

        //fire
        pg.fire(e);
        assertEquals(0, pg.getCleaners().size());
    }



    @Test
    public void buildBlock_ferrisWheel(){
        Position p=new Position(0,0,false);
        Game g=new Game(GameType.FERRISWHEEL,p);

        assertTrue(g.getSize().getX_asIndex()==2 && g.getSize().getY_asIndex()==2);
        assertTrue(engine.buildBlock(g));
        assertEquals(engine.getPg().blocks[0][0],g);
        assertEquals(engine.getPg().blocks[0][1],g);
        assertEquals(engine.getPg().blocks[1][0],g);
        assertEquals(engine.getPg().blocks[1][1],g);
    }

    @Test
    public void buildBlock_usedPlace(){
        Position p=new Position(0,0,false);
        Block b0=new Game(GameType.ROLLERCOASTER,p);
        assertTrue(engine.buildBlock(b0));
        assertEquals(engine.getPg().blocks[0][0],b0);
        assertEquals(engine.getPg().blocks[0][1],b0);
        assertEquals(engine.getPg().blocks[0][2],b0);
        assertEquals(engine.getPg().blocks[1][0],b0);
        assertEquals(engine.getPg().blocks[1][1],b0);
        assertEquals(engine.getPg().blocks[1][2],b0);


        Block b1=new Game(GameType.FERRISWHEEL,new Position(0,0,false));
        Block b2=new Game(GameType.FERRISWHEEL,new Position(0,1,false));
        Block b3=new Game(GameType.FERRISWHEEL,new Position(1,0,false));
        Block b4=new Game(GameType.FERRISWHEEL,new Position(1,1,false));
        Block b5=new Game(GameType.FERRISWHEEL,new Position(2,0,false));
        Block b6=new Game(GameType.FERRISWHEEL,new Position(2,1,false));

        assertFalse(engine.buildBlock(b1));
        assertFalse(engine.buildBlock(b2));
        assertFalse(engine.buildBlock(b3));
        assertFalse(engine.buildBlock(b4));
        assertFalse(engine.buildBlock(b5));
        assertFalse(engine.buildBlock(b6));
    }

    @Test
    public void buildBlock_demolishGame(){
        //build
        Position p=new Position(0,0,false);
        Block b=new Game(GameType.FERRISWHEEL,p);
        assertTrue(engine.buildBlock(b));
        assertEquals(engine.getPg().blocks[0][0],b);

        //demolish
        engine.demolish(b);
        assertTrue(engine.getPg().blocks[0][0] instanceof FreePlace);
    }
}
