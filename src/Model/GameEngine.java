package Model;

import Model.Blocks.*;
import Model.People.*;

import java.util.*;

import static View.MainWindow2.NUM_OF_COLS;
import static View.MainWindow2.NUM_OF_ROWS;

/**
 * GameEngine osztály felel a legtöbb számítás elvégzésében,
 * a timerek elindításában és adatok feldolgozásában és továbbításában.
 */
public class GameEngine {
    /* Adattagok */
    private Playground pg;
    private boolean isBuildingPeriod;

    public static final int TIME_1x = 5;
    private int minutesPerSecond = TIME_1x;
    private int prevVisitors = 1;

    private final Random rnd = new Random();

    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        isBuildingPeriod = true;

        for (int i = 0; i < NUM_OF_COLS; i++)
            for (int j = 0; j < NUM_OF_ROWS; j++)
                buildBlockImmediately(new FreePlace(new Position(i, j, false)));

        //Alap tesztpálya építése---------------------------------------------------------------------------------------
        buildBlockImmediately(new Road(new Position(5, 0, false), false, true));
        buildBlockImmediately(new Road(new Position(5, 1, false)));
        buildBlockImmediately(new Road(new Position(5, 2, false)));
        buildBlockImmediately(new Road(new Position(5, 3, false)));
        buildBlockImmediately(new Road(new Position(5, 4, false)));
        buildBlockImmediately(new Road(new Position(5, 5, false)));
        buildBlockImmediately(new Road(new Position(6, 5, false)));
        buildBlockImmediately(new Road(new Position(7, 5, false)));
        buildBlockImmediately(new Road(new Position(8, 5, false)));
        buildBlockImmediately(new Road(new Position(9, 5, false)));
        Road dirtyRoad = new Road(new Position(10, 5, false));
        dirtyRoad.setGarbage(40);
        buildBlockImmediately(dirtyRoad);
        buildBlockImmediately(new Road(new Position(10, 6, false)));
        buildBlockImmediately(new Road(new Position(10, 7, false)));
        buildBlockImmediately(new Road(new Position(10, 8, false)));
        buildBlockImmediately(new Road(new Position(10, 9, false)));
        buildBlockImmediately(new Road(new Position(10, 10, false)));
        buildBlockImmediately(new Road(new Position(10, 11, false), false, true));
        Game repairme = new Game(GameType.FERRISWHEEL, new Position(6, 6, false));
        repairme.setCondition(10);
        buildBlockImmediately(repairme);
        buildBlock(new Game(GameType.ROLLERCOASTER, new Position(11, 8, false)));
        ServiceArea buffet = new ServiceArea(ServiceType.BUFFET, new Position(6, 2, false));
        buffet.hire(new Caterer(buffet.getPos(), 10, buffet), getPg());
        buildBlockImmediately(buffet);
        buildBlockImmediately(new ServiceArea(ServiceType.TOILET, new Position(9, 8, false)));
        buildBlockImmediately(new EmployeeBase(new Position(11, 5, false)));
        //--------------------------------------------------------------------------------------------------------------

        //Első tesztalkalmazottak felvétele-----------------------------------------------------------------------------
        Cleaner cl = new Cleaner(new Position(5, 0, false), 10);
        Repairman re = new Repairman(new Position(5, 0, false), 10);
        pg.hire(cl);
        pg.hire(re);
        //--------------------------------------------------------------------------------------------------------------
    }


    /* Metódusok */

    /**
     * Overloadolt változat: Minden megépítendő block ezen a függvényen keresztül fogja elérni a fő építő függvényt.
     *
     * @param toBuild Átadott block amit meg szeretnénk építeni
     * @return True: Ha építés végbement / nem történt hiba építés közben.
     * False: Ha valamilyen hiba lépett fel építés alatt. (Például: Foglalt helyre szeretnénék építeni)
     */
    public boolean buildBlock(Block toBuild) {
        return buildBlock(toBuild, false);
    }

    /**
     * Függvény segítségével tudjuk könnyen, több sor megspórolásával megépíteni manuálisan a tesztpályát.
     *
     * @param toBuild Átadott block amit meg szeretnénk építeni
     * @return True: Ha építés végbement / nem történt hiba építés közben.
     * False: Ha valamilyen hiba lépett fel építés alatt. (Például: Foglalt helyre szeretnénék építeni)
     */
    public boolean buildBlockImmediately(Block toBuild) {
        return buildBlock(toBuild, true);
    }

    /**
     * Fő építő függvény. Ennek a függvény segítségével lehet építkezni a játékban. Paraméterben átadott megépítendő
     * block függvényében, végigmegyünk a balfelső sarkától a jobb alsó sarkáig, és egyesével megépítjük az elemeket
     * a Playground építőfüggvény segítségével.
     * Miután sikerült az építés, a blockot egy adatszerkezetbe helyezzük attól függően, milyen típusú.
     *
     * @param toBuild      Átadott block amit meg szeretnénk építeni
     * @param instantBuild Ha igaz, abban az esetben azonnal felépül az block, amit építeni szeretnénk.
     * @return True: Ha építés végbement / nem történt hiba építés közben.
     * False: Ha valamilyen hiba lépett fel építés alatt. (Például: Foglalt helyre szeretnénék építeni)
     */
    public boolean buildBlock(Block toBuild, boolean instantBuild) {
        //if(!isBuildingPeriod) { System.err.println("Nem lehet építkezni, míg nyitva van a park!"); return false; }

        if (pg.getMoney() < toBuild.getBuildingCost()) return false;
        if (!(pg.isBuildable(toBuild))) return false;
        if (toBuild instanceof GarbageCan) return buildBin(toBuild.pos);

        int posFromX = toBuild.getPos().getX_asIndex();
        int posFromY = toBuild.getPos().getY_asIndex();
        int buildUntilX = posFromX + toBuild.getSize().getX_asIndex();
        int buildUntilY = posFromY + toBuild.getSize().getY_asIndex();

        if (toBuild instanceof Road && pg.blocks[posFromX][posFromY] instanceof Road)
            if (((Road) toBuild).isEntrance() != ((Road) pg.blocks[posFromX][posFromY]).isEntrance())
                ((Road) pg.blocks[posFromX][posFromY]).setEntrance(!((Road) pg.blocks[posFromX][posFromY]).isEntrance());

        for (int x = posFromX; x < buildUntilX; ++x)
            for (int y = posFromY; y < buildUntilY; ++y)
                pg.buildBlock(toBuild, x, y);

        if (instantBuild) toBuild.buildInstantly();
        else toBuild.build();

        if (toBuild instanceof Game) pg.getBuildedGameList().add((Game) toBuild);
        else if (toBuild instanceof ServiceArea) pg.getBuildedServiceList().add((ServiceArea) toBuild);
        else if (toBuild instanceof EmployeeBase) pg.getBuildedEmployeeBases().add((EmployeeBase) toBuild);

        pg.setMoney(pg.getMoney() - toBuild.getBuildingCost());
        pg.getBuildedObjectList().add(toBuild);
        return true;
    }

    /**
     * A függvény segítségével a balfelső saroktól a jobbalsó sarkoig lehet építményeket törölni.
     * Ha sikeres a törlés, kiveszük a megfelelő adatszerkezetből az építményt.
     *
     * @param deleteBlockHere Egy pozíció, ahonnan törölni szeretnénk a kiválasztott építményt.
     */
    public void demolish(Position deleteBlockHere) {
        //if(!isBuildingPeriod)             return;

        int posFromX = deleteBlockHere.getX_asIndex();
        int posFromY = deleteBlockHere.getY_asIndex();

        Block toDelete = pg.getBlocks()[posFromX][posFromY];
        if (toDelete instanceof FreePlace) return;

        int demolishUntilX = posFromX + toDelete.getSize().getX_asIndex();
        int demolishUntilY = posFromY + toDelete.getSize().getY_asIndex();

        for (int x = posFromX; x < demolishUntilX; ++x)
            for (int y = posFromY; y < demolishUntilY; ++y)
                pg.demolishBlock(x, y);


        pg.getBuildedObjectList().remove(toDelete);

        if (toDelete instanceof Game)
            pg.getBuildedGameList().remove(toDelete);
        else if (toDelete instanceof ServiceArea)
            pg.getBuildedServiceList().remove(toDelete);
    }

    /**
     * Kukát lehet lehelyezni az útra vagy eltávolítani, ha van rajta
     *
     * @param p pozíció ahova kattintottunk
     * @return true: Ha útra kattintuttunk
     * false: Ha nem útra kattintottunk
     */
    public boolean buildBin(Position p) {
        if (pg.blocks[p.getX_asIndex()][p.getY_asIndex()] instanceof Road) {
            if (((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).isEntrance()) return false;

            ((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()])
                    .setHasGarbageCan(!(((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).isHasGarbageCan()));

            return true;
        }
        return false;
    }

    /**
     * Függvény segítségével elindíthatjuk a szimulációt.
     * A függvényben elindul két timer, az egyik 16 ms-onként fut le, ez felel a visitorok és munkások mozgásáért és
     * a közvetlen szimulációs folyamatok végbementeléért. A másik timer pedig másodpercenként fut le, ez felel az idő
     * megfelelő teléséért és a blockok / visitorok roundHasPassed metódus meghívásáért.
     */
    public void startDay() {
        if (!(pg.getHours() == 8)) return;

        isBuildingPeriod = false;
        pg.getBuildedObjectList().forEach(Block::startDay);

        Position entrancePosition = pg.getRandomEntrance(rnd).getPos();
        for (int idx = 0; idx < prevVisitors; idx++)
            pg.getVisitors().add(new Visitor(entrancePosition));

        Timer personTimer = new Timer();
        Timer simulationTimer = new Timer();

        personTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Block> copy = new ArrayList<>(pg.getBuildedObjectList());
                try {
                    boolean everyCleanerHasJob = false;

                    for (Block b : copy) {
                        if (b instanceof Road) {
                            Road road = ((Road) b);
                            Road.GarbageLevel garbageLevel = road.getGarbageLevel();

                            if ((garbageLevel == Road.GarbageLevel.FEW || garbageLevel == Road.GarbageLevel.LOT)
                                    && Objects.isNull(road.cleaner)) {

                                Cleaner cleaner = pg.getFreeCleaner();

                                if (Objects.isNull(pg.getFreeCleaner()))    everyCleanerHasJob = true;
                                if (!Objects.isNull(cleaner)) {
                                    road.cleaner = cleaner;
                                    cleaner.goal = road;
                                    cleaner.setupRoute(pg);
                                }
                            }
                        }
                    }

                    if (!everyCleanerHasJob) {
                        ArrayList<EmployeeBase> bases = pg.getBuildedEmployeeBases();
                        Cleaner cleaner = pg.getFreeCleaner();

                        while (!Objects.isNull(cleaner)) {
                            cleaner.goal = bases.get(Math.abs(rnd.nextInt() % bases.size()));
                            cleaner.setupRoute(pg);
                            cleaner = pg.getFreeCleaner();
                        }
                    }

                    for (Cleaner v : pg.getCleaners()) {
                        if (Objects.isNull(v.goal) && !v.isBusy())
                            v.findGoal(rnd, pg);

                        if (!Objects.isNull(v.goal) && !v.isMoving && !v.isBusy())
                            v.setupRoute(pg);
                        else if (v.isMoving && !v.isBusy())
                            v.move(minutesPerSecond);
                    }

                    for (Repairman v : pg.getRepairmen()) {
                        if (Objects.isNull(v.goal) && !v.isBusy())
                            v.findGoal(rnd, pg);

                        if (!Objects.isNull(v.goal) && !v.isMoving && !v.isBusy())
                            v.setupRoute(pg);
                        else if (v.isMoving && !v.isBusy())
                            v.move(minutesPerSecond);

                    }
                    for (Visitor v : pg.getVisitors()) {
                        if (Objects.isNull(v.goal)) {
                            v.findGoal(rnd, pg);
                            if (!Objects.isNull(v.goal))
                                v.setupRoute(pg);

                        } else
                            v.move(minutesPerSecond);
                    }

                    if (pg.getPopularity() + 3 >= pg.getVisitors().size())  chanceToVisitorisComing();

                } catch (ConcurrentModificationException e) {
                    System.err.println("Error: " + e.toString());
                }
            }
        }, 0, 16);


        simulationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pg.setMinutes(pg.getMinutes() + minutesPerSecond);

                for (Block b : pg.getBuildedObjectList())
                    b.roundHasPassed(minutesPerSecond);
                for (Cleaner c : pg.getCleaners())
                    c.roundHasPassed(minutesPerSecond);
                for (Repairman c : pg.getRepairmen())
                    c.roundHasPassed(minutesPerSecond);

                ArrayList<Visitor> visitorsCopy = new ArrayList<>(pg.getVisitors());
                for (Visitor v : visitorsCopy) {
                    v.roundHasPassed(minutesPerSecond);

                    int throwgarbage = Math.abs(rnd.nextInt() % 100);
                    if (throwgarbage > 93 && !pg.isGarbageCanNearby(v.getPosition())) {
                        Block possibleroad = pg.getBlockByPosition(v.getPosition());
                        if (possibleroad instanceof Road) {
                            ((Road) possibleroad).setGarbage(((Road) possibleroad).getGarbage() + 15);
                            ((Road) possibleroad).cleaner = null;
                        }
                    }

                    if (pg.isDecorationNearby(v.getPosition()))
                        v.setHappiness(v.getHappiness() + 1);

                    if (pg.getBlockByPosition(v.getPosition()) instanceof Road
                            && ((Road) pg.getBlockByPosition(v.getPosition())).getGarbageLevel() == Road.GarbageLevel.LOT)
                        v.setHappiness(v.getHappiness() - 3);


                    if (v.getState() == VisitorState.WANNA_LEAVE && pg.getBlockByPosition(v.getPosition()) instanceof Road) {
                        Road r = (Road) pg.getBlockByPosition(v.getPosition());
                        if (r.isEntrance()) {
                            visitorPayTheirCredit(v);
                            updateParkPopularity(v);
                            pg.getVisitors().remove(v);
                        }
                    }
                }

                if (pg.getMinutes() >= 60) {
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours() + 1);
                }
                if (pg.getHours() >= 20) {
                    prevVisitors = endDay();
                }
                if (pg.getHours() >= 24) {
                    pg.setHours(24);
                    pg.setMinutes(0);
                }

                if (pg.getVisitors().size() == 0) {
                    pg.setMinutes(0);
                    pg.setHours(8);
                    pg.setDays(pg.getDays() + 1);
                    isBuildingPeriod = true;

                    personTimer.cancel();
                    personTimer.purge();
                    simulationTimer.cancel();
                    simulationTimer.purge();
                }
            }
        }, 0, 1000);
    }

    /**
     * Metódus fele a látogatók érkezésért a popularity függvényében.
     */
    private void chanceToVisitorisComing() {
        if (pg.getPopularity() >= 100)      pg.setPopularity(100);
        else if (pg.getPopularity() <= 0)   pg.setPopularity(0);

        int randomPeriod = rnd.nextInt(100 - 1) + 1;
        double currentPopularity = pg.getPopularity();
        double periodPoint = currentPopularity + randomPeriod + minutesPerSecond;

        if (pg.getHours() < 20) {
            if (pg.getPopularity() <= 0 && periodPoint >= 90) addVisitor();
            else if (pg.getPopularity() >= 1 && pg.getPopularity() < 25 && periodPoint >= 100) addVisitor();
            else if (pg.getPopularity() >= 25 && pg.getPopularity() < 75 && periodPoint >= 120) addVisitor();
            else if (pg.getPopularity() >= 75 && periodPoint >= 140) addVisitor();
        }
    }

    /**
     * Hozzáad egy látogatót a parkhoz
     */
    private void addVisitor() {
        pg.getVisitors().add(new Visitor(pg.getRandomEntrance(rnd).getPos()));
        pg.getVisitors().get(pg.getVisitors().size() - 1).roundHasPassed(minutesPerSecond);
        pg.setMoney(pg.getMoney() + 10);
        System.out.println("Visitor érkezett és fizetett 10$-t a belépőért!");
    }

    /**
     * Növeli vagy csökkenti az adott visitor távakozásakor a Popularityt.
     * @param v Visitor aki távozott a parkból
     */
    private void updateParkPopularity(Visitor v) {
        if (v.getHappiness() >= 50) pg.setPopularity(pg.getPopularity() + 1);
        else if (v.getHappiness() < 50) pg.setPopularity(pg.getPopularity() - 1);
        else if (v.getHappiness() >= 100) pg.setPopularity(pg.getPopularity() + 2);
        else if (v.getHappiness() <= 0) pg.setPopularity(pg.getPopularity() - 2);
    }

    /**
     * Visitor kifizeti az általa igénybe vett szolgálatásokat
     * @param v Visitor aki távozott a parkból
     */
    private void visitorPayTheirCredit(Visitor v) {
        if (v.getCredit() > 0) {
            pg.setMoney(pg.getMoney() + v.getCredit());
            v.setCredit(0);
        }
    }

    /**
     * Metódus lecsökkenti a játékos pénzét a nap végén upkeep costnyival
     * Minden block a nap végén veszít 1 conditiont
     */
    private void endDayPayOff() {
        pg.getBuildedObjectList().forEach(Block::endDay);
        int money = pg.getMoney();
        for (Block b : pg.getBuildedObjectList()) {
            money -= b.getUpkeepCost();
            b.setCondition(b.getCondition() - 1);
        }
        money -= getSalaries();
        pg.setMoney(money);

    }

    private int endDay() {
        endDayPayOff();
        int ret = pg.getVisitors().size();
        for (Visitor v : pg.getVisitors())
            v.setStayingTime(-1000);
        return ret;
    }

    /* Getterek / Setterek */
    public Playground getPg()                           { return pg; }
    public int getRepairmanSalaries()                   { return pg.getRepairmen().stream().mapToInt(Employee::getSalary).sum(); }
    public int getCatererSalaries()                     { return pg.getCaterers().stream().mapToInt(Employee::getSalary).sum(); }
    public int getOperatorSalaries()                    { return pg.getOperators().stream().mapToInt(Employee::getSalary).sum(); }
    public int getClenanerSalaries()                    { return pg.getCleaners().stream().mapToInt(Employee::getSalary).sum(); }
    public int getSalaries()                            { return getClenanerSalaries() + getOperatorSalaries()
                                                                    + getRepairmanSalaries() + getCatererSalaries(); }
    public boolean isBuildingPeriod()                   { return isBuildingPeriod; }
    public void setTimerSpeed(int minutesPerSecond)     { this.minutesPerSecond = minutesPerSecond; }


}
