package Model;

import Model.Blocks.*;
import Model.People.*;

import java.util.*;

import static View.MainWindow2.NUM_OF_COLS;
import static View.MainWindow2.NUM_OF_ROWS;

//TODO: Szimuláció folyatása

public class GameEngine {
    /* Adattagok */
    private Playground pg;
    private boolean isBuildingPeriod;
    public static final int TIME_1x=5;
    private int minutesPerSecond = TIME_1x;
    private int prevVisitors = 1;

    private final Random rnd = new Random();

    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        isBuildingPeriod = true;

        for(int i = 0; i < NUM_OF_COLS; i++) {
            for(int j = 0; j < NUM_OF_ROWS; j++) {
                buildBlockImmediately(new FreePlace(new Position(i,j,false)));
            }
        }

        //test: Base Gamefield
        buildBlockImmediately(new Road(new Position(5,0,false),false,true));
        buildBlockImmediately(new Road(new Position(5,1,false)));
        buildBlockImmediately(new Road(new Position(5,2,false)));
        buildBlockImmediately(new Road(new Position(5,3,false)));
        buildBlockImmediately(new Road(new Position(5,4,false)));
        buildBlockImmediately(new Road(new Position(5,5,false)));
        buildBlockImmediately(new Road(new Position(6,5,false)));
        buildBlockImmediately(new Road(new Position(7,5,false)));
        buildBlockImmediately(new Road(new Position(8,5,false)));
        buildBlockImmediately(new Road(new Position(9,5,false)));
        //buildBlock(new Road(new Position(10,5,false)));
        Road dirtyRoad=new Road(new Position(10,5,false));
        dirtyRoad.setGarbage(40);
        buildBlockImmediately(dirtyRoad);
        buildBlockImmediately(new Road(new Position(10,6,false)));
        buildBlockImmediately(new Road(new Position(10,7,false)));
        buildBlockImmediately(new Road(new Position(10,8,false)));
        buildBlockImmediately(new Road(new Position(10,9,false)));
        buildBlockImmediately(new Road(new Position(10,10,false)));
        buildBlockImmediately(new Road(new Position(10,11,false),false,true));
        Game repairme = new Game(GameType.FERRISWHEEL,new Position(6,6,false));
        repairme.setCondition(10);
        buildBlockImmediately(repairme);
        buildBlock(new Game(GameType.ROLLERCOASTER,new Position(11,8,false)));
        ServiceArea buffet=new ServiceArea(ServiceType.BUFFET,new Position(6,2,false));
        buffet.hire(new Caterer(buffet.getPos(),10,buffet),getPg());
        buildBlockImmediately(buffet);
        buildBlockImmediately(new ServiceArea(ServiceType.TOILET,new Position(9,8,false)));
        buildBlockImmediately(new EmployeeBase(new Position(11,5,false)));

        //hire
        Cleaner cl=new Cleaner(new Position(5,0,false),10);
        Repairman re=new Repairman(new Position(5,0,false),10);
        pg.hire(cl);
        pg.hire(re);
    }


    /* Metódusok */
    /**
     * Metódus segítségével megépítjük a blockot és behlyezzük a mátrxiba.
     * Először leellenőrizzük, hogy építhető-e size-ja szerint, ha igen megépítjük.
     * Végül hozzáadunk egy példányt a megépített block-ok listájába a szimuláció érdekében.
     * @param toBuild megépítendő block
     * @return  false, ha egyik blockban nem freeplace van
     *          true, ha építés végbement
     */
    public boolean buildBlock(Block toBuild) {
        return buildBlock(toBuild,false);
    }

    public boolean buildBlockImmediately(Block toBuild) {
        return buildBlock(toBuild,true);
    }

    public boolean buildBlock(Block toBuild,boolean instantBuild){
        //if(!isBuildingPeriod) { System.err.println("Nem lehet építkezni, míg nyitva van a park!"); return false; }

        if(pg.getMoney() < toBuild.getBuildingCost()) return false;
        if(!(pg.isBuildable(toBuild))) return false;
        if(toBuild instanceof GarbageCan){return buildBin(toBuild.pos);}

        int posFromX = toBuild.getPos().getX_asIndex();
        int posFromY = toBuild.getPos().getY_asIndex();
        int buildUntilX = posFromX + toBuild.getSize().getX_asIndex();
        int buildUntilY = posFromY + toBuild.getSize().getY_asIndex();
        if(toBuild instanceof Road &&  pg.blocks[posFromX][posFromY] instanceof Road){
            if(((Road) toBuild).isEntrance() != ((Road) pg.blocks[posFromX][posFromY]).isEntrance()){
                ((Road) pg.blocks[posFromX][posFromY]).setEntrance(!((Road) pg.blocks[posFromX][posFromY]).isEntrance());
            }
        }


        for (int x = posFromX; x < buildUntilX; ++x)
            for (int y = posFromY; y < buildUntilY; ++y)
                pg.buildBlock(toBuild,x,y);


        pg.setMoney(pg.getMoney()-toBuild.getBuildingCost());
        pg.getBuildedObjectList().add(toBuild);
        if(instantBuild){
            toBuild.buildInstantly();
        }else{
            toBuild.build();
        }

        if(toBuild instanceof Game)               pg.getBuildedGameList().add((Game) toBuild);
        else if(toBuild instanceof ServiceArea)   { pg.getBuildedServiceList().add((ServiceArea) toBuild);
            System.out.println("Bekerült az objekt");}
        else if(toBuild instanceof EmployeeBase) {pg.getBuildedEmployeeBases().add((EmployeeBase)toBuild);}

        return true;
    }

    public void demolish(Position deleteBlockHere) {

        //if(!isBuildingPeriod) { System.err.println("Nem lehet építkezni, míg nyitva van a park!"); return; }
        int posFromX = deleteBlockHere.getX_asIndex();
        int posFromY = deleteBlockHere.getY_asIndex();
        Block toDelete = pg.getBlocks()[posFromX][posFromY];
        if(toDelete instanceof FreePlace){
            System.err.println("Cannot delete a FreePlace!"); return;
        }

        int demolishUntilX = posFromX + toDelete.getSize().getX_asIndex();
        int demolishUntilY = posFromY + toDelete.getSize().getY_asIndex();

        for(int x=posFromX; x<demolishUntilX; ++x) {
            for(int y=posFromY; y<demolishUntilY; ++y) {
                pg.demolishBlock( x, y);
            }
        }

        pg.getBuildedObjectList().remove(toDelete);

        if(toDelete instanceof Game) {
            pg.getBuildedGameList().remove(toDelete);
        } else if (toDelete instanceof ServiceArea) {
            pg.getBuildedServiceList().remove(toDelete);
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
            if(((Road) pg.blocks[p.getX_asIndex()][p.getY_asIndex()]).isEntrance()){return false;}// bejaratnal ne legyen kuka
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

    public void startDay()  {
        if(!(pg.getHours() == 8)) { System.err.println("A nap már elkezdődött!"); return; }
        isBuildingPeriod = false;
        pg.getBuildedObjectList().forEach(Block::startDay);

        Position entrancePosition = pg.getRandomEntrance(rnd).getPos();

        System.out.println("Nap elkezdődött!");

        for(int idx = 0; idx < prevVisitors; idx++ )
            pg.getVisitors().add(new Visitor(entrancePosition));

        Timer personTimer = new Timer();
        Timer simulationTimer = new Timer();

        personTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Block> copy= new ArrayList<>(pg.getBuildedObjectList());

                try {
                    //manage block
                    boolean everyCleanerHasJob = false;
                    for(Block b :copy){
                        if(b instanceof Road){
                            Road road=((Road) b);
                            Road.GarbageLevel garbageLevel =road.getGarbageLevel();
                            if((garbageLevel== Road.GarbageLevel.FEW || garbageLevel == Road.GarbageLevel.LOT) && Objects.isNull(road.cleaner)){
                                Cleaner cleaner=pg.getFreeCleaner();
                                if(Objects.isNull(pg.getFreeCleaner())) {
                                    everyCleanerHasJob = true;
                                }
                                if(!Objects.isNull(cleaner)){
                                    road.cleaner=cleaner;
                                    cleaner.goal=road;
                                    cleaner.setupRoute(pg);
                                }
                            }
                        }
                    }
                    // send cleaners back to base
                    if(!everyCleanerHasJob) {
                        ArrayList<EmployeeBase> bases = pg.getBuildedEmployeeBases();
                        Cleaner cleaner = pg.getFreeCleaner();
                        while(!Objects.isNull(cleaner)) {
                            if(!Objects.isNull(cleaner)){
                                cleaner.goal= bases.get(Math.abs(rnd.nextInt()%bases.size()));
                                cleaner.setupRoute(pg);
                            }
                            cleaner = pg.getFreeCleaner();
                        }
                    }


                    //manage cleaners
                    for (Cleaner v : pg.getCleaners()) {
                        if(Objects.isNull(v.goal) && !v.isBusy()){
                            v.findGoal(rnd, pg);
                        }
                        if(!Objects.isNull(v.goal) && !v.isMoving && !v.isBusy()) {
                            v.setupRoute(pg);
                        }
                        else if(v.isMoving && !v.isBusy()){v.move(minutesPerSecond);}
                        }

                    //manage repairmen
                    for (Repairman v : pg.getRepairmen()) {
                        if(Objects.isNull(v.goal) && !v.isBusy()){
                            v.findGoal(rnd, pg);
                        }
                        if(!Objects.isNull(v.goal) && !v.isMoving && !v.isBusy()) {
                            v.setupRoute(pg);
                        }
                        else if(v.isMoving && !v.isBusy()){v.move(minutesPerSecond);}
                    }
                    for (Visitor v : pg.getVisitors()) {
                            if(Objects.isNull(v.goal)){
                                v.findGoal(rnd,pg);
                                if(!Objects.isNull(v.goal)){
                                    v.setupRoute(pg);
                                }
                            } else {
                               v.move(minutesPerSecond);
                            }

                    }
                    if(pg.getPopularity()+3 >= pg.getVisitors().size())     chanceToVisitorisComing();

                } catch (ConcurrentModificationException e) { System.err.println("Concurrent"); }
            }
        },0,16);


        simulationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pg.setMinutes(pg.getMinutes() + minutesPerSecond);

                for(Block b : pg.getBuildedObjectList())
                    b.roundHasPassed(minutesPerSecond);
                for(Cleaner c : pg.getCleaners())
                    c.roundHasPassed(minutesPerSecond);
                for(Repairman c : pg.getRepairmen())
                    c.roundHasPassed(minutesPerSecond);

                ArrayList<Visitor> visitorsCopy= new ArrayList<>(pg.getVisitors());
                for(Visitor v :visitorsCopy) {
                    v.roundHasPassed(minutesPerSecond);

                    int throwgarbage = Math.abs(rnd.nextInt() % 100);
                    if(throwgarbage > 93 && !pg.isGarbageCanNearby(v.getPosition())) {
                        Block possibleroad = pg.getBlockByPosition(v.getPosition());
                        if(possibleroad instanceof Road) {
                            ((Road) possibleroad).setGarbage(((Road) possibleroad).getGarbage()+15);
                            ((Road) possibleroad).cleaner = null;
                        }
                    }
                    if (v.getState() == VisitorState.WANNA_LEAVE && pg.getBlockByPosition(v.getPosition()) instanceof Road ) {
                        Road r = (Road) pg.getBlockByPosition(v.getPosition());
                        if (r.isEntrance()) {
                            visitorPayTheirCredit(v);
                            updateParkPopularity(v);
                            pg.getVisitors().remove(v);
                        }
                    }
                }

                if(pg.getMinutes() >= 60)       { pg.setMinutes(0); pg.setHours(pg.getHours() + 1); }
                if(pg.getHours() >= 20)         { prevVisitors = endDay(); }
                if(pg.getHours() >= 24)         { pg.setHours(24); pg.setMinutes(0); }

                if(pg.getVisitors().size() == 0) {
                    pg.setMinutes(0);
                    pg.setHours(8);
                    pg.setDays(pg.getDays()+1);
                    isBuildingPeriod = true;

                    personTimer.cancel(); personTimer.purge();
                    simulationTimer.cancel(); simulationTimer.purge();
                }
            }}, 0, 1000);
    }

    void chanceToVisitorisComing() {
        if(pg.getPopularity() >= 100)       pg.setPopularity(100);
        else if(pg.getPopularity() <= 0)    pg.setPopularity(0);

        int randomPeriod = rnd.nextInt(100-1) + 1;
        double currentPopularity = pg.getPopularity();
        double periodPoint = currentPopularity + randomPeriod + minutesPerSecond;

        if(pg.getHours() < 20) {
            if(pg.getPopularity() <= 0 && periodPoint >= 90)                                        addVisitor();
            else if(pg.getPopularity() >= 1  && pg.getPopularity() < 25  && periodPoint >= 100)     addVisitor();
            else if(pg.getPopularity() >= 25 && pg.getPopularity() < 75  && periodPoint >= 120)     addVisitor();
            else if(pg.getPopularity() >= 75 && periodPoint >= 140)                                 addVisitor();
        }
        randomPeriod = 0; currentPopularity = 0; periodPoint = 0;
    }
    void addVisitor() {
        pg.getVisitors().add(new Visitor(pg.getRandomEntrance(rnd).getPos()));
        pg.getVisitors().get(pg.getVisitors().size() - 1).roundHasPassed(minutesPerSecond);
        pg.setMoney(pg.getMoney()+10);
        System.out.println("Visitor érkezett és fizetett 10$-t a belépőért!");
    }
    void updateParkPopularity(Visitor v) {
        if(v.getHappiness() >= 50)          pg.setPopularity(pg.getPopularity()+1);
        else if(v.getHappiness() < 50)      pg.setPopularity(pg.getPopularity()-1);
        else if(v.getHappiness() >= 100)    pg.setPopularity(pg.getPopularity()+2);
        else if(v.getHappiness() <= 0)      pg.setPopularity(pg.getPopularity()-2);
    }
    void visitorPayTheirCredit(Visitor v) {
        if(v.getCredit() > 0) {
            pg.setMoney(pg.getMoney() + v.getCredit());
            v.setCredit(0);
        }
    }

    /**
     * Metódus lecsökkenti a játékos pénzét a nap végén upkeep costnyival
     * Minden block a nap végén veszít 1 conditiont
     * További szimulációs lépések itt lesznek majd implementálhatók
     */
    public void endDayPayOff() {
        pg.getBuildedObjectList().forEach(Block::endDay);
        int money = pg.getMoney();
        for(Block b : pg.getBuildedObjectList()) {
            money -= b.getUpkeepCost();
            b.setCondition(b.getCondition()-1);
        }
        System.out.println("endPayOff msg: Építmények upkeep costjai ki lettek fizetve!");

        money -= getSalaries();
        System.out.println("endPayOff msg: Alkalmazottak ki lettek fizetve!");

        pg.setMoney(money);

    }

    public int endDay()
    {
        endDayPayOff();
        int ret = pg.getVisitors().size();
        for(Visitor v : pg.getVisitors())
            v.setStayingTime(-1000);
        return ret;
    }

    public int getRepairmanSalaries(){return pg.getRepairmen().stream().mapToInt(Employee::getSalary).sum(); }
    public int getCatererSalaries(){return pg.getCaterers().stream().mapToInt(Employee::getSalary).sum(); }
    public int getOperatorSalaries(){return pg.getOperators().stream().mapToInt(Employee::getSalary).sum(); }
    public int getClenanerSalaries(){return pg.getCleaners().stream().mapToInt(Employee::getSalary).sum(); }
    public int getSalaries(){return getClenanerSalaries() + getOperatorSalaries() + getRepairmanSalaries() + getCatererSalaries(); }

    /* Getterek / Setterek */
    public Playground getPg() { return pg; }

    public boolean isBuildingPeriod() {
        return isBuildingPeriod;
    }


    public  int getTimerSpeed(int minutesPerSecond) { return minutesPerSecond; }

    public  void setTimerSpeed(int minutesPerSecond) { this.minutesPerSecond = minutesPerSecond; }

}
