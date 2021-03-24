package Model;

import Model.Blocks.*;
import Model.People.*;

import java.util.Timer;
import java.util.TimerTask;


public class GameEngine {
    /* Adattagok */
    private Playground pg;
    private boolean isBuildingPeriod;

    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        isBuildingPeriod = true;
    }


    /* Metódusok */
    /**
     * Metódus segítségével megépítjük a blockot és behlyezzük a mátrxiba.
     * Először leellenőrizzük, hogy építhető-e size-ja szerint, ha igen megépítjük.
     * Végül hozzáadunk egy példányt a megépített block-ok listájába a szimuláció érdekében.
     * @param b megépítendő block
     * @return  false, ha egyik blockban nem freeplace van
     *          true, ha építés végbement
     */
    public boolean buildBlock(Block b) {
        if(!isBuildingPeriod) { System.err.println("Nem lehet építkezni, míg nyitva van a park!"); return false; }

        if(b instanceof GarbageCan){return buildBin(b.pos);}
        if(pg.getMoney() < b.getBuildingCost()) return false;
        if(!(pg.isBuildable(b))) return false;

        int posFromX = b.getPos().getX_asIndex();
        int posFromY = b.getPos().getY_asIndex();
        int buildUntilX = posFromX + b.getSize().getX_asIndex();
        int buildUntilY = posFromY + b.getSize().getY_asIndex();

        for (int x = posFromX; x < buildUntilX; ++x)
            for (int y = posFromY; y < buildUntilY; ++y)
                pg.buildBlock(b, x, y);

        pg.setMoney(pg.getMoney()-b.getBuildingCost());
        pg.getBuildedObjectList().add(b); System.out.println("BuildedObjectList-be bekerült a megépítendő block");
        return true;
    }

    boolean buildBin(Position p){//todo revise
        boolean r=false;
        if(pg.blocks[p.getX_asIndex()][p.getY_asIndex()] instanceof Road){
            r=true;
            ((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).setHasGarbageCan(true);
        }
        return r;
    }

    public void demolish(Block b) {
        int posFromX = b.getPos().getX_asIndex();
        int posFromY = b.getPos().getY_asIndex();
        int demolishUntilX = posFromX + b.getSize().getX_asIndex();
        int demolishUntilY = posFromY + b.getSize().getY_asIndex();

        for(int x=posFromX; x<demolishUntilX; ++x) {
            for(int y=posFromY; y<demolishUntilY; ++y) {
                Block freeplaceBlock = new FreePlace(0,0,0,BlockState.FREE);
                freeplaceBlock.setPos(new Position(x,y,false));
                pg.demolishBlock(freeplaceBlock, x, y);
            }
        }
        for(Block removedObject : pg.getBuildedObjectList()) {
            if(posFromX == removedObject.getPos().getX_asIndex() && posFromY == removedObject.getPos().getY_asIndex()) {
                pg.getBuildedObjectList().remove(removedObject);
                break;
            }
        }
    }
    /**
     * Metódus lecsökkenti a játékos pénzét a nap végén upkeep costnyival
     * Minden block a nap végén veszít 1 conditiont
     * További szimulációs lépések itt lesznek majd implementálhatók
     */
    public void endDayPayOff() {
        int money = pg.getMoney();
        for(Block b : pg.getBuildedObjectList()) {
            money -= b.getUpkeepCost();
            b.setCondition(b.getCondition()-1);
            //További szimulációs lépések...
        }
        System.out.println("endPayOff msg: Építmények upkepp costjai ki lettek fizetve!");

        for(Caterer caterer : pg.getCateres())          money -= caterer.getSalary();
        for(Cleaner cleaner : pg.getCleaners())         money -= cleaner.getSalary();
        for(Operator operator : pg.getOperators())      money -= operator.getSalary();
        for(Repairman repairman : pg.getRepairmen())    money -= repairman.getSalary();
        System.out.println("endPayOff msg: Alkalmazottak ki lettek fizetve!");

        pg.setMoney(money);
    }

    public void startDay(){
        if(!(pg.getHours() == 8)) { System.err.println("A nap már elkezdődött!"); return;  }

        isBuildingPeriod = false;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            int minutesPerSecond = setTimerSpeed(30);

            pg.setMinutes(pg.getMinutes() + minutesPerSecond);
            if(pg.getMinutes() >= 60) { // Eltelt 1 óra a játékban
                pg.setMinutes(0);
                pg.setHours(pg.getHours()+1);
            }
            if(pg.getHours() >= 20) { // Eltelt 1 nap a játékban
                pg.setMinutes(0);
                pg.setHours(8);
                pg.setDays(pg.getDays()+1);

                System.out.println("Nap vége! Mostantól lehet építkezni!");
                endDayPayOff(); //Nap végén lévő elszámolás
                timer.cancel(); timer.purge(); // Timer leállítása a nap végén
                isBuildingPeriod = true;
            }
            }
        }, 1000, 1000);
        System.out.println("A nap elkeződött!");
    }


    /*public void visitorsDemand() {
        for(Visitor v : pg.getVisitors()) {
            if(v.getPlayfulness() <= 50) {
                //GOTO: Nearest or for him the best Game block
                //After the game hunger++
                //If the best game block for him is not available happiness--
            }
            if(v.getHunger() >= 10) {
                //GOTO: Nearest or for him the best Service Area
                //After the serivice hunger = 0 &  If there is no garbage, garbageOnRoad++
                //                                 else garbageContent++
                //If service is not provided then he leaves & happiness--
                //
            }
            // Kell a Visitornak: Eltölthető idő a vidámparkban
            // Miután ez az idő lejár, happiness befolyásolja a popularity-t
        }
    }*/


    /* Getterek / Setterek */
    BlockState getBlockState(Block b) { return pg.getBlockState(b); }

    public Playground getPlayground()      { return pg; }
    public Playground getPg() { return pg; }

    public int getPlayerMoney()            { return pg.getMoney(); }
    public int getPlayerDaysPassedBy()     { return pg.getDays(); }
    public double getPlayerPopularity()    { return pg.getPopularity(); }


    public static int setTimerSpeed(int minutesPerSecond) { return minutesPerSecond; }

}
