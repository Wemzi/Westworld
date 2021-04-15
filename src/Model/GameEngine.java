package Model;

import Model.Blocks.*;
import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Visitor;

import java.util.*;

import static View.MainWindow2.NUM_OF_COLS;
import static View.MainWindow2.NUM_OF_ROWS;

//TODO: Szimuláció folyatása

public class GameEngine {
    /* Adattagok */
    private Playground pg;
    private boolean isBuildingPeriod;
    public static final int TIME_1x=5;
    private static int minutesPerSecond = TIME_1x;

    /* Konstruktor */
    public GameEngine() {
        pg = new Playground();
        isBuildingPeriod = true;

        for(int i = 0; i < NUM_OF_COLS; i++) {
            for(int j = 0; j < NUM_OF_ROWS; j++) {
                buildBlock(new FreePlace(new Position(i,j,false)));
            }
        }

        //test: Base Gamefield
        buildBlock(new Road(new Position(5,0,false),false,true));
        buildBlock(new Road(new Position(5,1,false)));
        buildBlock(new Road(new Position(5,2,false)));
        buildBlock(new Road(new Position(5,3,false)));
        buildBlock(new Road(new Position(5,4,false)));
        buildBlock(new Road(new Position(5,5,false)));
        buildBlock(new Road(new Position(6,5,false)));
        buildBlock(new Road(new Position(7,5,false)));
        buildBlock(new Road(new Position(8,5,false)));
        buildBlock(new Road(new Position(9,5,false)));
        //buildBlock(new Road(new Position(10,5,false)));
        Road dirtyRoad=new Road(new Position(10,5,false));
        dirtyRoad.setGarbage(40);
        buildBlock(dirtyRoad);
        buildBlock(new Road(new Position(10,6,false)));
        buildBlock(new Road(new Position(10,7,false)));
        buildBlock(new Road(new Position(10,8,false)));
        buildBlock(new Road(new Position(10,9,false)));
        buildBlock(new Road(new Position(10,10,false)));
        buildBlock(new Road(new Position(10,11,false),false,true));

        buildBlock(new Game(GameType.FERRISWHEEL,new Position(6,6,false)));
        buildBlock(new Game(GameType.ROLLERCOASTER,new Position(11,8,false)));
        buildBlock(new ServiceArea(ServiceType.BUFFET,new Position(6,2,false)));
        buildBlock(new ServiceArea(ServiceType.TOILET,new Position(9,8,false)));
        buildBlock(new EmployeeBase(new Position(4,4,false)));

        pg.entrancePosition = new Position(5,0,false);

        //hire
        Cleaner cl=new Cleaner(new Position(5,0,false),10);
        pg.hire(cl);
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
        //if(!isBuildingPeriod) { System.err.println("Nem lehet építkezni, míg nyitva van a park!"); return false; }

        if(toBuild instanceof GarbageCan){return buildBin(toBuild.pos);}
        if(pg.getMoney() < toBuild.getBuildingCost()) return false;
        if(!(pg.isBuildable(toBuild))) return false;

        int posFromX = toBuild.getPos().getX_asIndex();
        int posFromY = toBuild.getPos().getY_asIndex();
        int buildUntilX = posFromX + toBuild.getSize().getX_asIndex();
        int buildUntilY = posFromY + toBuild.getSize().getY_asIndex();

        for (int x = posFromX; x < buildUntilX; ++x)
            for (int y = posFromY; y < buildUntilY; ++y)
                pg.buildBlock(toBuild,x,y);


        pg.setMoney(pg.getMoney()-toBuild.getBuildingCost());
        pg.getBuildedObjectList().add(toBuild);
        toBuild.build();

        if(toBuild instanceof Game)               pg.getBuildedGameList().add((Game) toBuild);
        else if(toBuild instanceof ServiceArea)   { pg.getBuildedServiceList().add((ServiceArea) toBuild);
            System.out.println("Bekerült az objekt");}

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

        Position entrancePosition = pg.getEntrancePosition();
        pg.getVisitors().add(new Visitor(entrancePosition));
        //pg.getVisitors().get(0).roundHasPassed(minutesPerSecond);



        Timer visitorTimer = new Timer();
        Timer timer = new Timer();
        Random rnd = new Random();
        visitorTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                ArrayList<Block> copy= new ArrayList<>(pg.getBuildedObjectList());
                try{
                    //manage block

                    for(Block b :copy){
                        if(b instanceof Game){
                            ((Game) b).roundHasPassed(minutesPerSecond);
                        }else if(b instanceof ServiceArea){
                            ((ServiceArea) b).roundHasPassed(minutesPerSecond);
                        }else if(b instanceof Road){
                            Road road=((Road) b);
                            Road.GarbageLevel garbageLevel =road.getGarbageLevel();
                            if((garbageLevel== Road.GarbageLevel.FEW || garbageLevel == Road.GarbageLevel.LOT) && Objects.isNull(road.cleaner)){
                                Cleaner cleaner=pg.getFreeCleaner();
                                if(!Objects.isNull(cleaner)){
                                    road.cleaner=cleaner;
                                    cleaner.goal=road;
                                    cleaner.setupRoute(pg);
                                }
                            }
                        }
                    }


                    //manage cleaners
                    for (Cleaner v : pg.getCleaners()) {
                        v.roundHasPassed(minutesPerSecond);
                        if(Objects.isNull(v.goal) && !v.isBusy()){
                            v.findGoal(rnd,pg);
                            if(!Objects.isNull(v.goal)){
                                v.setupRoute(pg);
                            }
                        }else{
                            if(v.isMoving){v.move(minutesPerSecond);}
                        }
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
                for(Visitor v : pg.getVisitors()) {
                        v.roundHasPassed(minutesPerSecond);
                        //System.out.println(v.toString());

                    int throwgarbage = Math.abs(rnd.nextInt() % 100);
                    if(throwgarbage > 93)
                    {
                        Block possibleroad = pg.getBlockByPosition(v.getPosition());
                        if(possibleroad instanceof Road)
                        {
                            ((Road) possibleroad).setGarbage(((Road) possibleroad).getGarbage()+15);
                        }
                    }
                    if (v.getStayingTime() == 0) {
                        pg.getVisitors().remove(v);
                        if (v.getHappiness() >= 50) {
                            pg.setPopularity(pg.getPopularity() + 1);
                        } else {
                            pg.setPopularity(pg.getPopularity() - 1);
                        }
                        break;
                    }
                }
                rounds[1] = 0;

                if(pg.getMinutes() >= 60) { // Eltelt 1 óra a játékban
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours()+1);

                    pg.getVisitors().add(new Visitor(entrancePosition));
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
