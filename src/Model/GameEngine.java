package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.FreePlace;
import Model.People.Visitor;

import java.util.Timer;
import java.util.TimerTask;


public class GameEngine {
    /* Adattagok */
    private Timer timer;
    private Playground pg;
    // TODO: Ki kellene találni, hogyan fogunk végigiterálni a Person-ökön, és meghívni a megfelelő metódusaikat
    // TODO: -||- A blokkokkal, cél a szimuláció elindítása. OK
    // TODO: A person-öket mozgatni kéne, annak függvényében, mire vágynak. (playfulness, hunger). Elég ha teleportálnak.
    // TODO: Kéne kezdőpénz, és le kell vonni az összeget, amikor építünk valamit. Legyen nagyon sok pénzünk kezdetben. OK
    // TODO: Jó lenne letesztelni a játékok run() metódusát, hogy működik-e a cooldown
    // TODO: Töltsük fel a pályát FreePlace-ekkel a játék kezdetekor OK
    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int minutesPerSecond = setTimerSpeed(10);

                pg.setMinutes(pg.getMinutes() + minutesPerSecond);
                if(pg.getMinutes() >= 60) { // Eltelt 1 óra a játékban
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours()+1);
                }
                if(pg.getHours() >= 20) { // Eltelt 1 nap a játékban
                    pg.setMinutes(0);
                    pg.setHours(8);
                    pg.setDays(pg.getDays()+1);

                    endDayPayOff(); //Nap végén lévő elszámolás
                }
            }
        }, 1000, 1000);
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

    public void demolish(Block b) {
        int posFromX = b.getPos().getX_asIndex();
        int posFromY = b.getPos().getY_asIndex();
        int demolishUntilX = posFromX + b.getSize().getX_asIndex();
        int demolishUntilY = posFromY + b.getSize().getY_asIndex();

        Block freeplaceBlock = new FreePlace(0,0,0,BlockState.FREE);
        for(int x=posFromX; x<demolishUntilX; ++x) {
            for(int y=posFromY; y<demolishUntilY; ++y) {
                pg.demolishBlock(freeplaceBlock, x, y);
            }
        }
        pg.setMoney(pg.getMoney()+(b.getBuildingCost()/2));
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
        pg.setMoney(money);
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


    void setTimerOff() { timer.cancel(); System.out.println("Timer leállítva!"); }

    public static int setTimerSpeed(int minutesPerSecond) { return minutesPerSecond; }

}
