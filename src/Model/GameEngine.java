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
    public static int TIME_1x=5;

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
        buildBlock(new Road(new Position(10,5,false)));
        buildBlock(new Road(new Position(10,6,false)));
        buildBlock(new Road(new Position(10,7,false)));
        buildBlock(new Road(new Position(10,8,false)));
        buildBlock(new Road(new Position(10,9,false)));
        buildBlock(new Road(new Position(10,10,false)));
        buildBlock(new Road(new Position(10,11,false),false,true));

        pg.entrancePosition = new Position(5,0,false);
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
                pg.buildBlock(b,x,y);

        b.setState(BlockState.UNDER_CONSTRUCTION);

        pg.setMoney(pg.getMoney()-b.getBuildingCost());
        pg.getBuildedObjectList().add(b);

        if(b instanceof Game)               pg.getBuildedGameList().add((Game) b);
        else if(b instanceof ServiceArea)   pg.getBuildedServiceList().add((ServiceArea) b);

        return true;
    }

    public void demolish(Block b) {
        int posFromX = b.getPos().getX_asIndex();
        int posFromY = b.getPos().getY_asIndex();
        int demolishUntilX = posFromX + b.getSize().getX_asIndex();
        int demolishUntilY = posFromY + b.getSize().getY_asIndex();

        Block demolishedBlock = pg.getBlockByPosition(new Position(posFromX,posFromY,false));

        for(int x=posFromX; x<demolishUntilX; ++x) {
            for(int y=posFromY; y<demolishUntilY; ++y) {
                FreePlace freeplaceBlock = new FreePlace(0,0,0,BlockState.FREE);
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

        if(demolishedBlock instanceof Game) {
            for(Block removedObject : pg.getBuildedGameList()) {
                if(posFromX == removedObject.getPos().getX_asIndex() && posFromY == removedObject.getPos().getY_asIndex()) {
                    pg.getBuildedGameList().remove(b);
                    break;
                }
            }
        } else if (demolishedBlock instanceof ServiceArea) {
            for(Block removedObject : pg.getBuildedServiceList()) {
                if(posFromX == removedObject.getPos().getX_asIndex() && posFromY == removedObject.getPos().getY_asIndex()) {
                    pg.getBuildedServiceList().remove(b);
                    break;
                }
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

    public void startDay()  {
        if(!(pg.getHours() == 8)) { System.err.println("A nap már elkezdődött!"); return; }

        Position entrancePosition = pg.getEntrancePosition();
        pg.getVisitors().add(new Visitor(entrancePosition));


        isBuildingPeriod = false;
        int minutesPerSecond = setTimerSpeed(5);

        Timer visitorTimer = new Timer();
        Timer timer = new Timer();
        Random rnd = new Random();
        visitorTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (Visitor v : pg.getVisitors()) {
                        if(v.isBusy()) continue;
                        Position wheretogo = null;
                        Block interactwithme = null;
                        if (!v.isMoving && v.getState().equals(VisitorState.WANNA_PLAY)) {
                            ArrayList<Game> GameList = pg.getBuildedGameList();
                            if (GameList.size() == 0) break;
                            wheretogo = GameList.get(Math.abs((rnd.nextInt())) % GameList.size()).getPos();
                            pg.findRoute(v, v.getPosition(), wheretogo);
                            v.pathPositionIndex = v.getPathPositionList().size()-1;
                            v.isMoving = true;
                            System.out.println(v.getPathPositionList());
                        }
                        else if (!v.isMoving && v.getState().equals(VisitorState.WANNA_EAT)) {
                            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
                            if (SvList.size() == 0) break;
                            for(ServiceArea svarea : SvList)
                            {
                                if(svarea.getType().equals(ServiceType.BUFFET)) {
                                    wheretogo = svarea.getPos();
                                    interactwithme = svarea;
                                    break;
                                }
                            }
                            if(wheretogo == null) break;
                            pg.findRoute(v, v.getPosition(), wheretogo);
                            v.pathPositionIndex = v.getPathPositionList().size()-1;
                            v.isMoving = true;
                            System.out.println(v.getPathPositionList());
                        }
                        else if (!v.isMoving && v.getState().equals(VisitorState.WANNA_TOILET)) {
                            ArrayList<ServiceArea> SvList = pg.getBuildedServiceList();
                            if (SvList.size() == 0) break;
                            for(ServiceArea svarea : SvList)
                            {
                                if(svarea.getType().equals(ServiceType.TOILET)) {
                                    wheretogo = svarea.getPos();
                                    interactwithme = svarea;
                                    break;
                                }
                            }
                            if(wheretogo == null) break;
                            pg.findRoute(v, v.getPosition(), wheretogo);
                            v.pathPositionIndex = v.getPathPositionList().size()-1;
                            v.isMoving = true;
                            System.out.println(v.getPathPositionList());
                        }
                        if (v.isMoving) {
                            boolean isArrived =  v.getPathPositionList().size()  == 0 || (v.getPosition().getX_asPixel() == v.getPathPositionList().get(0).getX_asPixel() &&
                                    v.getPosition().getY_asPixel() == v.getPathPositionList().get(0).getY_asPixel());
                            if(isArrived) {
                                continue;
                            }
                            Position nextBlockPosition = null;
                                    if(v.getPathPositionList().size() > 0) nextBlockPosition = v.getPathPositionList().get(v.pathPositionIndex);
                            boolean isSamePosition = v.getPosition().getX_asPixel() == nextBlockPosition.getX_asPixel()
                                    && v.getPosition().getY_asPixel() == nextBlockPosition.getY_asPixel();
                            boolean isDifferentPosition = v.getPosition().getX_asPixel() != nextBlockPosition.getX_asPixel()
                                    || v.getPosition().getY_asPixel() != nextBlockPosition.getY_asPixel();
                            boolean goingRight = v.getPathPositionList().size()  != 0 && nextBlockPosition.getX_asPixel() > v.getPosition().getX_asPixel();
                            boolean goingLeft = v.getPathPositionList().size()  != 0  && nextBlockPosition.getX_asPixel() < v.getPosition().getX_asPixel();
                            boolean goingUp = nextBlockPosition.getY_asPixel() > v.getPosition().getY_asPixel();
                            boolean goingDown = nextBlockPosition.getY_asPixel() < v.getPosition().getY_asPixel();

                            if (isArrived) {
                                v.isMoving = false;
                                ArrayList<Position> copy = v.getPathPositionList();
                                v.getPathPositionList().removeAll(copy);
                                if(v.getState().equals(VisitorState.WANNA_TOILET) &&  interactwithme != null)
                                {
                                    v.toilet((ServiceArea) interactwithme);
                                }
                                else if(v.getState().equals(VisitorState.WANNA_PLAY) && interactwithme != null)
                                {
                                    v.playGame( (Game) interactwithme);
                                }
                                else if(v.getState().equals(VisitorState.WANNA_EAT) && interactwithme != null)
                                {
                                    v.eat( (ServiceArea) interactwithme);
                                }
                            }

                            if (isSamePosition) {
                                v.pathPositionIndex--;
                            }
                            else if (isDifferentPosition) {

                                if (goingRight) {
                                    v.setPosition(new Position(v.getPosition().getX_asPixel() + minutesPerSecond/2, v.getPosition().getY_asPixel(), true));
                                }
                                if (goingLeft) {
                                    v.setPosition(new Position(v.getPosition().getX_asPixel() - minutesPerSecond/2, v.getPosition().getY_asPixel(), true));
                                }
                                if (goingUp) {
                                    v.setPosition(new Position(v.getPosition().getX_asPixel(), v.getPosition().getY_asPixel() + minutesPerSecond/2, true));
                                }
                                if (goingDown) {
                                    v.setPosition(new Position(v.getPosition().getX_asPixel(), v.getPosition().getY_asPixel() - minutesPerSecond/2, true));
                                }
                            }
                        }
                    }
                } catch (ConcurrentModificationException e){}
            }
        },0,16);

        final int[] vistorsComingPeriod = {5};
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pg.setMinutes(pg.getMinutes() + minutesPerSecond);

                for(Game g: getPg().getBuildedGameList()){
                    g.roundHasPassed(minutesPerSecond);
                }

                for(Visitor v : pg.getVisitors()) {
                    v.setStayingTime(v.getStayingTime() - minutesPerSecond);
                    v.roundHasPassed(minutesPerSecond);
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

                if(pg.getMinutes() >= 60) { // Eltelt 1 óra a játékban
                    pg.setMinutes(0);
                    pg.setHours(pg.getHours()+1);

                    pg.getVisitors().add(new Visitor(entrancePosition));
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

    public boolean isBuildingPeriod() {
        return isBuildingPeriod;
    }


    public  int setTimerSpeed(int minutesPerSecond) { return minutesPerSecond; }

}
