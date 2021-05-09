package Model;

import Model.Blocks.*;
import Model.People.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

import static View.GameWindow.NUM_OF_COLS;
import static View.GameWindow.NUM_OF_ROWS;


/**
 * Playground osztály felel az adatok inicializásálért, adatszerkezetek létrehozásáért és az adatok tárolásáért.
 * Minden egyes elemi lépés itt történik, és minden egyes adat itt kerül átadásra és szállításra
 */
public class Playground {

    /* Adattagok */
    public Block[][] blocks;
    private ArrayList<Block> buildedObjects;
    private final ArrayList<Game> builedGames;
    private final ArrayList<ServiceArea> builededServices;
    private final ArrayList<EmployeeBase> buildedEmployeeBases;
    private final ArrayList<Visitor> visitors;

    private final ArrayList<Caterer> caterers;
    private final ArrayList<Cleaner> cleaners;
    private final ArrayList<Repairman> repairmen;

    private int money;
    private int days, hours, minutes;
    private double popularity;
    private boolean bankruptcy;

    /* Konstruktor */
    public Playground() {
        blocks = new Block[NUM_OF_COLS][NUM_OF_ROWS];
        buildedObjects = new ArrayList<>();
        builedGames = new ArrayList<>();
        builededServices = new ArrayList<>();
        visitors = new ArrayList<>();
        caterers = new ArrayList<>();
        cleaners = new ArrayList<>();
        repairmen = new ArrayList<>();
        buildedEmployeeBases = new ArrayList<>();

        money = 100_000;
        days = 1;
        hours = 8;
        minutes = 0;
        popularity = 0;
        bankruptcy = false;
    }

    /* Metódusok */

    /**
     * Metódus segítségével X,Y pozícióba lehet egy objektumot építeni
     *
     * @param block : Megépítendő block
     * @param posX  X.-edik pozíció ahová építeni kell
     * @param posY  Y.-adik pozíció ahová építeni kell
     * @return false: HA adott blockon már van valami építve
     * true: Ha adott blockon nincs még semmi építve
     */
    public boolean buildBlock(Block block, int posX, int posY) {
        if (!(blocks[posX][posY] instanceof FreePlace) && !Objects.isNull(blocks[posX][posY])) return false;

        buildedObjects.remove(blocks[posX][posY]);
        blocks[posX][posY] = block;
        return true;
    }

    /**
     * Metódus segítésgével lehetséges az X,Y pozícióból egy blokkot törölni.
     *
     * @param posX X.-edik pozíció ahonnan törölni kell
     * @param posY Y.-adik pozíció ahonnan törölni kell
     */
    public void demolishBlock(int posX, int posY) {
        FreePlace freeplaceBlock = new FreePlace(0, 0, 0, BlockState.FREE);
        freeplaceBlock.setPos(new Position(posX, posY, false));
        blocks[posX][posY] = freeplaceBlock;
    }

    /**
     * Megkapjuk a blockot, annak a pozíció és size alapján kiszámítjuk mettől meddig tart, majd végigmegyünk
     * a blockokon, ha valahol nem freeplace van visszatérünk false-szal
     *
     * @param block Megépítendő block
     * @return False egyből, ha már az első helyen nem freeplace van
     * False literálisan, ha valamelyik helyen nem freeplace van
     * True ha az összesen helyen freeplace van
     */
    public boolean isBuildable(Block block) {
        int blockFromX = block.getPos().getX_asIndex();
        int blockFromY = block.getPos().getY_asIndex();

        if (blockFromX < 0 || blockFromX >= NUM_OF_COLS || blockFromY < 0 || blockFromY >= NUM_OF_ROWS) return false;

        if (Objects.isNull(blocks[block.getPos().getX_asIndex()][block.getPos().getY_asIndex()]))       return true;

        if (block instanceof GarbageCan) {
            if (blocks[block.getPos().getX_asIndex()][block.getPos().getY_asIndex()] instanceof Road) {
                if (((Road) blocks[block.getPos().getX_asIndex()][block.getPos().getY_asIndex()]).isEntrance()) {
                    return false;
                }
                return !((Road) blocks[block.getPos().getX_asIndex()][block.getPos().getY_asIndex()]).isHasGarbageCan();
            }
            return false;
        }

        if (block instanceof Road && ((Road) block).isEntrance()) {
            int x = block.getPos().getX_asIndex();
            int y = block.getPos().getY_asIndex();
            Block replaceThis = blocks[x][y];

            if (!(replaceThis instanceof Road || replaceThis instanceof FreePlace)) return false;
            if (replaceThis instanceof Road && ((Road) replaceThis).isEntrance())   return false;

            return x == 0 || y == 0 || x == NUM_OF_COLS - 1 || y == NUM_OF_ROWS - 1;
        }
        if (!(blocks[block.getPos().getX_asIndex()][block.getPos().getY_asIndex()] instanceof FreePlace)) return false;


        int blockMaxX = blockFromX + block.getSize().getX_asIndex();
        int blockMaxY = blockFromY + block.getSize().getY_asIndex();

        if (blockMaxX > NUM_OF_COLS || blockMaxY > NUM_OF_ROWS) return false;

        for (int x = blockFromX; x < blockMaxX; ++x)
            for (int y = blockFromY; y < blockMaxY; ++y)
                if (!(blocks[x][y] instanceof FreePlace)) return false;

        return true;
    }

    /**
     * Metódus segítségével lehet felbérelni új alkalmazottat
     * @param e alkalmazott akit fel akarunk bérelni
     */
    public void hire(Employee e) {
        if (e instanceof Caterer) {
            caterers.add((Caterer) e);
            ((Caterer) e).workPlace.addWorker((Caterer) e);
        } else if (e instanceof Cleaner) {
            cleaners.add((Cleaner) e);
        } else if (e instanceof Repairman) {
            repairmen.add((Repairman) e);
            System.out.println("Hired a repairman");
        }
    }

    /**
     * Metódus segítésgével lehet elbocsájtani egy alkalmazottat
     * @param e az elbocsjátandó alkalmazott
     */
    public void fire(Employee e) {
        if (e instanceof Caterer) {
            caterers.remove(e);
            ((Caterer) e).workPlace.getWorkers().remove(e);

        } else if (e instanceof Cleaner) {
            Cleaner cl = getFreeCleaner();

            if (!Objects.isNull(cl))    cleaners.remove(cl);
            else                        System.err.println("No free cleaner to remove!");

        } else if (e instanceof Repairman) {
            Repairman r = getFreeRepman();

            if (!Objects.isNull(r))     repairmen.remove(r);
            else                        System.err.println("No free repairman to remove!");
        }
    }

    /**
     * Segítésgével egy megadott Person képes megtalálni az céljához vezető utat koordináta páronként
     * @param visitor aki keresi az utat
     * @param start jelenlegi pozíció ahol van jelenleg a Person
     * @param destination a célpozíció, ahová el szeretni jutni
     * @return  True, ha vezet oda út
     *          False, ha nem vezet oda út
     */
    public boolean findRoute(Person visitor, Position start, Position destination) {
        boolean[][] visited = new boolean[NUM_OF_COLS][NUM_OF_ROWS];

        for (int i = 0; i < NUM_OF_COLS; i++)
            for (int j = 0; j < NUM_OF_ROWS; j++)
                if (i == start.getX_asIndex() && j == start.getY_asIndex() && !visited[i][j])
                    if (isPath(i, j, visited, start, destination, visitor)) {
                        visitor.getPathPositionList().add(new Position(i, j, false));
                        return true;
                    }
        return false;
    }

    private boolean isSafe(int i, int j) { return i >= 0 && i < blocks.length && j >= 0 && j < blocks[0].length; }

    private boolean isPath(int i, int j, boolean[][] visited, Position start, Position destination, Person visitor) {
        if (isSafe(i, j)
                && (    (blocks[i][j] instanceof Road && blocks[i][j].getState() != BlockState.UNDER_CONSTRUCTION)
                        || blocks[i][j] instanceof Game
                            && blocks[i][j].getPos().getX_asIndex() == destination.getX_asIndex()
                            && blocks[i][j].getPos().getY_asIndex() == destination.getY_asIndex()
                        || blocks[i][j] instanceof ServiceArea
                            && blocks[i][j].getPos().getX_asIndex() == destination.getX_asIndex()
                            && blocks[i][j].getPos().getY_asIndex() == destination.getY_asIndex()
                        || blocks[i][j] instanceof EmployeeBase
                            && blocks[i][j].getPos().getX_asIndex() == destination.getX_asIndex()
                            && blocks[i][j].getPos().getY_asIndex() == destination.getY_asIndex()
                        || blocks[i][j] instanceof Game
                            && blocks[i][j].getPos().getX_asIndex() == start.getX_asIndex()
                            && blocks[i][j].getPos().getY_asIndex() == start.getY_asIndex()
                        || blocks[i][j] instanceof ServiceArea
                            && blocks[i][j].getPos().getX_asIndex() == start.getX_asIndex()
                            && blocks[i][j].getPos().getY_asIndex() == start.getY_asIndex()
                        || blocks[i][j] instanceof EmployeeBase
                            && blocks[i][j].getPos().getX_asIndex() == start.getX_asIndex()
                            && blocks[i][j].getPos().getY_asIndex() == start.getY_asIndex()
                    )
                && !visited[i][j]) {

            visited[i][j] = true;

            if (i == destination.getX_asIndex() && j == destination.getY_asIndex()) {
                visitor.getPathPositionList().add(new Position(i, j, false));
                return true;
            }

            boolean down = isPath(i + 1, j, visited, start, destination, visitor);
            if (down) {
                visitor.getPathPositionList().add(new Position(i, j, false));
                return true;
            }

            boolean up = isPath(i - 1, j, visited, start, destination, visitor);
            if (up) {
                visitor.getPathPositionList().add(new Position(i, j, false));
                return true;
            }

            boolean right = isPath(i, j + 1, visited, start, destination, visitor);
            if (right) {
                visitor.getPathPositionList().add(new Position(i, j, false));
                return true;
            }

            boolean left = isPath(i, j - 1, visited, start, destination, visitor);
            if (left) {
                visitor.getPathPositionList().add(new Position(i, j, false));
                return true;
            }

        }
        return false;
    }

    /**
     * Metódus segítésgével lekérdező hogy van-e a közelben kuka
     * @param pos Pozíció ahonnan mérünk
     * @return  True, ha van
     *          False, ha nincs
     */
    public boolean isGarbageCanNearby(Position pos) {
        int posXorigin = pos.getX_asIndex();
        int posYorigin = pos.getY_asIndex();
        int posX = pos.getX_asIndex() - 2;
        while (posX < posXorigin + 2) {
            int posY = pos.getY_asIndex() - 2;
            while (posY < posYorigin + 2) {
                if (isSafe(posX, posY)) {
                    Block b = blocks[posX][posY];
                    if (b instanceof Road && ((Road) b).isHasGarbageCan()) {
                        return true;
                    }
                }
                posY++;
            }
            posX++;
        }
        return false;
    }

    /**
     * Metódus segítésgével lekérdező hogy van-e a közelben dekoráció
     * @param pos Pozíció ahonnan mérünk
     * @return  True, ha van
     *          False, ha nincs
     */
    public boolean isDecorationNearby(Position pos) {
        int posXorigin = pos.getX_asIndex();
        int posYorigin = pos.getY_asIndex();
        int posX = pos.getX_asIndex() - 2;
        while (posX < posXorigin + 2) {
            int posY = pos.getY_asIndex() - 2;
            while (posY < posYorigin + 2) {
                if (isSafe(posX, posY)) {
                    Block b = blocks[posX][posY];
                    if (b instanceof Decoration) {
                        return true;
                    }
                }
                posY++;
            }
            posX++;
        }
        return false;
    }

    /* Getterek / Setterek */

    /**
     * Metódus segítésgével visszakaphatunk egy adatszerkezet, amelyben szerepel a parméterül kapott alkalmazott.
     * @param e Az alkalmazott akit keresünk
     * @return  Adatszerkezet amelyikben megtalálható az alkalmazott
     *          Null, ha az alkalmazott egyik osztályba se sorolható
     */
    public ArrayList getEmployeesLike(Employee e) {
        if (e instanceof Caterer)   return getCaterers();
        if (e instanceof Cleaner)   return getCleaners();
        if (e instanceof Operator)  return getOperators();
        if (e instanceof Repairman) return getRepairmen();
        return null;
    }

    /**
     * Metóus segítésgével kikereshetünk egy szabad takarítót, aki éppen nem dolgozik
     * @return  Visszaadja a takarítót, ha van.
     *          Ha nincs szabad takarító, a visszatérési érték NULL
     */
    public Cleaner getFreeCleaner() {
        for (Cleaner c : getCleaners())
            if (Objects.isNull(c.goal))
                return c;

        return null;
    }
    /**
     * Metóus segítésgével kikereshetünk egy szabad szerelőt, aki éppen nem dolgozik
     * @return  Visszaadja a szerelőt, ha van.
     *          Ha nincs szabad szerelő, a visszatérési érték NULL
     */
    public Repairman getFreeRepman() {
        for (Repairman r : getRepairmen()) {
            if (Objects.isNull(r.goal)) {
                return r;
            }
        }
        return null;
    }

    public ArrayList<Employee> getEmployees() {
        ArrayList<Employee> r = new ArrayList<>();
        r.addAll(getCaterers());
        r.addAll(getCleaners());
        r.addAll(getOperators());
        r.addAll(getRepairmen());
        return r;
    }
    public ArrayList<Operator> getOperators() {
        ArrayList<Operator> r = new ArrayList<>();
        getBuildedGameList().forEach(g -> r.addAll(g.getWorkers()));
        return r;
    }

    public Road getRandomEntrance(Random rnd) {
        ArrayList<Road> entrances = new ArrayList<>();
        getBuildedObjectList().forEach(r -> {
            if (r instanceof Road && ((Road) r).isEntrance() && r.getState() == BlockState.FREE) {
                entrances.add(((Road) r));
            }
        });
        if (entrances.size() == 0) {
            return null;
        }
        return entrances.get(Math.abs(rnd.nextInt()) % entrances.size());
    }

    public ArrayList<Person> getPeople() {
        ArrayList<Person> people = new ArrayList<>();
        people.addAll(getVisitors());
        people.addAll(getCaterers());
        people.addAll(getRepairmen());
        people.addAll(getCleaners());
        return people;
    }

    public int getMoney()                                               { return money; }
    public int getHours()                                               { return hours; }
    public int getMinutes()                                             { return minutes; }
    public int getDays()                                                { return days; }
    public double getPopularity()                                       { return popularity; }
    public Block getBlockByPosition(Position pos)                       { return blocks[pos.getX_asIndex()][pos.getY_asIndex()]; }
    public Color getColor(Block block)                                  { return block.getColor(); }
    public Block[][] getBlocks()                                        { return blocks; }
    public java.util.List<Block> getBuildedObjectList()                 { return Collections.synchronizedList(buildedObjects); }
    public ArrayList<Game> getBuildedGameList()                         { return builedGames; }
    public ArrayList<ServiceArea> getBuildedServiceList()               { return builededServices; }
    public java.util.List<Visitor> getVisitors()                        { return Collections.synchronizedList(visitors);}
    public ArrayList<Caterer> getCaterers()                             { return caterers; }
    public ArrayList<Cleaner> getCleaners()                             { return cleaners; }
    public ArrayList<Repairman> getRepairmen()                          { return repairmen; }
    public ArrayList<EmployeeBase> getBuildedEmployeeBases()            { return buildedEmployeeBases; }

    public void setMoney(int money)                                     { this.money = money; }
    public void setDays(int days)                                       { this.days = days; }
    public void setHours(int hours)                                     { this.hours = hours; }
    public void setMinutes(int minutes)                                 { this.minutes = minutes; }
    public void setPopularity(double popularity)                        { this.popularity = popularity; }


    /**
     * Szöveges formában jeleníti meg az időt
     * @return Szöveges formában a játékidő
     */
    public String dateToString() {
        return " Day: " + this.days +
                " Hour: " + this.hours +
                " Minutes: " + this.minutes;
    }
}
