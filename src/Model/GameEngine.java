package Model;

import Model.Blocks.*;
import Model.People.*;

import javax.print.attribute.standard.Destination;
import java.util.Timer;
import java.util.TimerTask;

import static View.MainWindow2.NUM_OF_COLS;


//TODO: Külön adatszerkezet minden egyes objektumhoz
//TODO: Static metódusok paraméterrel run-nal
//TODO: Szimuláció folyatása

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
    //TODO: UNDER_CONST ha épül
    public boolean buildBlock(Block b) {
        //if(!isBuildingPeriod) { System.err.println("Nem lehet építkezni, míg nyitva van a park!"); return false; }

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

        b.setState(BlockState.UNDER_CONSTRUCTION);

        pg.setMoney(pg.getMoney()-b.getBuildingCost());
        pg.getBuildedObjectList().add(b); System.out.println("BuildedObjectList-be bekerült a megépítendő block");
        return true;
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
     * Kukát lehet lehelyezni az útra vagy eltávolítani, ha van rajta
     * @param p pozíció ahova kattintottunk
     * @return  true: Ha útra kattintuttunk
     *          false: Ha nem útra kattintottunk
     */
    public boolean buildBin(Position p){
        if(pg.blocks[p.getX_asIndex()][p.getY_asIndex()] instanceof Road){
            if(!(((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).isHasGarbageCan())) {
                ((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).setHasGarbageCan(true);
                System.out.println("Kuka lehelyezve!");
            }
            else {
                ((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).setHasGarbageCan(false);
                System.out.println("Kuka eltávolítva!");
            }
            return true;
        }
        return false;
    }

    public void startDay(){
        if(!(pg.getHours() == 8)) { System.err.println("A nap már elkezdődött!"); return; }

        Position entrancePosition = pg.getEntrancePosition();

        isBuildingPeriod = false;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                int minutesPerSecond = setTimerSpeed(10);
                pg.setMinutes(pg.getMinutes() + minutesPerSecond);

                for(Visitor v : pg.getVisitors()) {
                    v.setStayingTime(v.getStayingTime()-minutesPerSecond); // Lecsökkentjük a staying timeot a visitoroknak
                    if(v.getStayingTime() == 0) {
                        pg.getVisitors().remove(v);
                        if (v.getHappiness() >= 50) {
                            pg.setPopularity(pg.getPopularity() + 1);
                        } else {
                            pg.setPopularity(pg.getPopularity() - 1);
                        }
                        break;
                    }
                    // TODO: ÖTLET: mi lenne ha inkább az isbusy-t használnád? Szerintem felesleges mindegyik activityre egy külön metódus.
                    // TODO: a fontossági sorrendet úgy is eltudjuk dönteni, hogy az egyik ifet előrébb tesszük mint a másikat. többi meetingen.
                    if(v.getPlayfulness() >= 50) {
                        //TODO: if(v.isPlaying()) { akkor ez fut le ->
                        // Van ilyen, v.isBusy néven
                        for(Block b : pg.getBuildedObjectList()) {
                            if(b instanceof Game) {
                                v.setPosition(new Position(b.getPos().getX_asIndex(),b.getPos().getY_asIndex(),false));
                                //TODO: playgame() eat()
                                //TODO: RoundHasPassed()

                                //TODO: v.setHappiens(v.getHappiens()+1);
                                // setHappiness implementálva, getHappiness van
                                //TODO: v.setPlayfullness(v.getPlayFullness()-10)
                                // setPlayfulness implementálva, getPlayfulness van
                                //TODO: v.setHunger(v.getHunger()+1)
                                // setHunger implementálva, gethunger van
                            } else {
                                // TODO: v.setHappiens(v.getHappiens()-10);
                                // setHappiness implementálva, getHappiness van
                            }
                            break;
                        }
                    }
                    else if(v.getHunger() >= 50) {
                        //TODO: if(v.inServiceBuilding() { akkor ez fut le ->
                        for(Block b: pg.getBuildedObjectList()) {
                            if(b instanceof ServiceArea) {
                                v.setPosition(new Position(b.getPos().getX_asIndex(),b.getPos().getY_asIndex(),false));
                                //TODO: v.setHappiens(v.getHappiens()+1);
                                //TODO: v.setPlayfullness(v.getPlayFullness()+10)
                                //TODO: v.setHunger(v.getHunger()-5)
                            } else {
                                //v.setHappiness - 10
                                pg.getVisitors().remove(v); // Elmegy ha nem kap kiszolgálást!
                            }
                            break;
                        }
                    }
                }

                if(pg.getMinutes() >= 60) { // Eltelt 1 óra a játékban
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours()+1);

                    pg.getVisitors().add(new Visitor(entrancePosition));
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
            }}, 1000, 1000);
        System.out.println("A nap elkeződött!");
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

            //TODO: Buggernyiós exceptiont dob egyelőre
            /*for(Visitor v : pg.getVisitors()) {
                pg.getVisitors().remove(v);
            }*/
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


    /* Getterek / Setterek */
    public Playground getPg() { return pg; }

    public static int setTimerSpeed(int minutesPerSecond) { return minutesPerSecond; }

}
