package Model;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.FreePlace;
import Model.People.*;

import java.awt.*;
import java.util.ArrayList;

import static View.MainWindow2.NUM_OF_COLS;
import static View.MainWindow2.NUM_OF_ROWS;

//TODO: Visitor happiness, befolyásolja a Popularity-t, amikor elmegy a parkból a visitor
//TODO: Visitor teleportáljon a játékokhoz

public class Playground {
    private static int MAX_VISITOR = 10;
    private static int MAX_EMPLOYEES = 5;
    /* Adattagok */
    public Block[][] blocks;
    private ArrayList<Block> buildedObjects;

    private ArrayList<Visitor> visitors;

    private ArrayList<Caterer> cateres;
    private ArrayList<Cleaner> cleaners;
    private ArrayList<Operator> operators;
    private ArrayList<Repairman> repairmen;

    private int money;
    private int days, hours, minutes;
    private double popularity;

    /* Konstruktor */
    public Playground() {
        blocks              = new Block[NUM_OF_COLS][NUM_OF_ROWS];
        buildedObjects      = new ArrayList<>();
        visitors            = new ArrayList<>();
        cateres             = new ArrayList<>();
        cleaners            = new ArrayList<>();
        operators           = new ArrayList<>();
        repairmen           = new ArrayList<>();

        for(int i = 0; i < NUM_OF_COLS; i++) {
            for(int j = 0; j < NUM_OF_ROWS; j++) {
                blocks[i][j] = new FreePlace(0,0,0,BlockState.FREE);
                blocks[i][j].pos = new Position(i,j,false);
            }
        }

        money = 100_000;
        days = 1; hours = 8; minutes = 0;
        popularity = 0;

        //testVisitor
        visitors.add(new Visitor(new Position(10,10,false)));
    }

    /* Metódusok */

    /**
     * Metódus segítségével X,Y pozícióba lehet egy objektumot építeni
     * @param block: Megépítendő block
     * @param posX: GE-ből megkapott X pozíció ahová építeni kell
     * @param posY: GE-ből megkapott Y pozíció ahová építeni kell
     * @return  false: HA adott blockon már van valami építve
     *          true: Ha adott blockon nincs még semmi építve
     */
    public boolean buildBlock(Block block, int posX, int posY) {
        if(!(blocks[posX][posY] instanceof FreePlace)) return false;
        blocks[posX][posY] = block;
        return true;
    }
    public void demolishBlock(Block block, int posX, int posY) {
        blocks[posX][posY] = block;
    }

    /**
     * Megkapjuk a blockot, annak a pozíció és size alapján kiszámítjuk mettől meddig tart, majd végigmegyünk
     * a blockokon, ha valahol nem freeplace van visszatérünk false-szal
     * @param block Megépítendő block
     * @return  False egyből, ha már az első helyen nem freeplace van
     *          False literálisan, ha valamelyik helyen nem freeplace van
     *          True ha az összesen helyen freeplace van
     */
    public boolean isBuildable(Block block){
        if(!(blocks[block.getPos().getX_asIndex()][block.getPos().getY_asIndex()] instanceof FreePlace)) return false;

        int blockFromX = block.getPos().getX_asIndex();
        int blockFromY = block.getPos().getY_asIndex();
        int blockMaxX = blockFromX + block.getSize().getX_asIndex();
        int blockMaxY = blockFromY + block.getSize().getY_asIndex();

        for(int x=blockFromX; x<blockMaxX; ++x)
            for(int y=blockFromY; y<blockMaxY; ++y)
                if(!(blocks[x][y] instanceof FreePlace)) return false;
        return true;
    }

    public boolean hire(Employee e) {
        int salary = e.getSalary();
        if(money < salary) return false;

        if(e instanceof Caterer) {
            //cateres.add(new Caterer(new Position(0, 0, false), salary));
            // TODO: implement new constructors
            return true;
        } else if(e instanceof Cleaner) {
            cleaners.add(new Cleaner(new Position(0,0,false), salary));
            return true;
        }
        else if(e instanceof Operator) {
            //operators.add(new Operator(new Position(0,0,false), salary));
            // TODO: implement new constructors
            return true;
        }
        else if(e instanceof Repairman) {
            repairmen.add(new Repairman(new Position(0,0,false), salary));
            return true;
        }
        else return false;
    }


    /* Getterek / Setterek */
    public int getMoney()                           { return money; }
    public int getHours()                           { return hours; }
    public int getMinutes()                         { return minutes; }
    public int getDays()                            { return days; }
    public double getPopularity()                   { return popularity; }

    public BlockState getBlockState(Block block)    { return block.getState(); }
    public Color getColor(Block block)              { return block.getColor(); }
    public Block[][] getBlocks()                    { return blocks; }
    public ArrayList<Block> getBuildedObjectList()  { return buildedObjects; }

    public ArrayList<Visitor> getVisitors()         { return visitors; }
    public ArrayList<Caterer> getCateres()          { return cateres; }
    public ArrayList<Cleaner> getCleaners()         { return cleaners; }
    public ArrayList<Operator> getOperators()       { return operators; }
    public ArrayList<Repairman> getRepairmen()      { return repairmen; }

    public void setMoney(int money)                 { this.money = money; }
    public void setDays(int days)                   { this.days = days; }
    public void setHours(int hours)                 { this.hours = hours; }
    public void setMinutes(int minutes)             { this.minutes = minutes; }
    public void setPopularity(double popularity)    { this.popularity = popularity; }
    public void setBuildedObjects(ArrayList<Block> buildedObjects) { this.buildedObjects = buildedObjects; }

    public String dateToString() {
        return  " Day: " + this.days +
                " Hour: " + this.hours +
                " Minutes: " + this.minutes;
    }
}
