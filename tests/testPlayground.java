import Model.Blocks.*;
import Model.People.Caterer;
import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Repairman;
import Model.Playground;
import Model.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class testPlayground {

    private Playground pg;

    @Before
    public void createPg(){pg=new Playground();}

    @Test
    public void buildBlock_game(){
        Position p=new Position(0,0,false);
        Block b=new Game(GameType.FERRISWHEEL,p);
        assertTrue(pg.buildBlock(b,0,0));
        assertEquals(pg.blocks[0][0],b);
    }

    @Test
    public void buildBlock_usedPlace(){
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
        assertTrue(pg.blocks[0][0] instanceof FreePlace);
        assertTrue(pg.isBuildable(b));
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
        pg.buildBlock(buffet,p.getX_asIndex(),p.getY_asIndex());

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
        pg.buildBlock(buffet,p.getX_asIndex(),p.getY_asIndex());

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

}
