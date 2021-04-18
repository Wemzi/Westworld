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
        pg.getVisitors().add(new Visitor(entrancePosition));
        //pg.getVisitors().get(0).roundHasPassed(minutesPerSecond);



        Timer visitorTimer = new Timer();
        Timer timer = new Timer();
        visitorTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                ArrayList<Block> copy= new ArrayList<>(pg.getBuildedObjectList());
                try{
                    //manage block
                    boolean everyCleanerHasJob = false;
                    for(Block b :copy){
                        //b.roundHasPassed(minutesPerSecond);
                        if(b instanceof Road){
                            Road road=((Road) b);
                            Road.GarbageLevel garbageLevel =road.getGarbageLevel();
                            if((garbageLevel== Road.GarbageLevel.FEW || garbageLevel == Road.GarbageLevel.LOT) && Objects.isNull(road.cleaner)){
                                Cleaner cleaner=pg.getFreeCleaner();
                                if(Objects.isNull(pg.getFreeCleaner()))
                                {
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
                    if(!everyCleanerHasJob)
                    {
                        ArrayList<EmployeeBase> bases = pg.getBuildedEmployeeBases();
                        Cleaner cleaner = pg.getFreeCleaner();
                        while(!Objects.isNull(cleaner))
                        {
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
                            }else{
                               v.move(minutesPerSecond);
                            }
                    }

                } catch (ConcurrentModificationException e){
                    System.err.println("Concurrent");
                }

            }
        },0,16);

        int[] rounds = {0,0};
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pg.setMinutes(pg.getMinutes() + minutesPerSecond);
                rounds[0] += minutesPerSecond; rounds[1] += minutesPerSecond;

                for(Game g: getPg().getBuildedGameList()){
                    if(rounds[0] >= 10) {
                        g.roundHasPassed(minutesPerSecond);
                    }
                }
                rounds[0] = 0;
                for(Block b: pg.getBuildedObjectList()){b.roundHasPassed(minutesPerSecond);}
                for(Visitor v : pg.getVisitors()) {
                        v.roundHasPassed(minutesPerSecond);
                        //System.out.println(v.toString());

                    int throwgarbage = Math.abs(rnd.nextInt() % 100);
                    if(throwgarbage > 93 && !pg.isGarbageCanNearby(v.getPosition()))
                    {
                        Block possibleroad = pg.getBlockByPosition(v.getPosition());
                        if(possibleroad instanceof Road)
                        {
                            ((Road) possibleroad).setGarbage(((Road) possibleroad).getGarbage()+15);
                        }
                    }
                    if (v.getStayingTime() == 0) {
                        pg.getVisitors().remove(v);
                        break;
                    }
                }

                for(Cleaner c : pg.getCleaners())
                {
                    c.roundHasPassed(minutesPerSecond);
                }
                rounds[1] = 0;

                if(pg.getMinutes() >= 60) { // Eltelt 1 óra a játékban
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours()+1);

                    pg.getVisitors().add(new Visitor(pg.getRandomEntrance(rnd).getPos()));
                    pg.getVisitors().get(pg.getVisitors().size()-1).roundHasPassed(minutesPerSecond);
                }
                if(pg.getHours() >= 20) { // Eltelt 1 nap a játékban
                    pg.setMinutes(0);
                    pg.setHours(8);
                    pg.setDays(pg.getDays()+1);

                    timer.cancel(); timer.purge(); // Timer leállítása a nap végén
                    visitorTimer.cancel(); visitorTimer.purge(); // Visitor timer leállítása
                    endDayPayOff(); //Nap végén lévő elszámolás
                    isBuildingPeriod = true;
                    System.out.println("Nap véget ért!");
                }
            }}, 0, 1000);
        System.out.println("A nap elkeződött!");
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
