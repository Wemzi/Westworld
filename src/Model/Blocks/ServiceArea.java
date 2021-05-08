package Model.Blocks;

import Model.People.Caterer;
import Model.People.Employee;
import Model.People.Visitor;
import Model.Playground;
import Model.Position;
import View.spriteManagers.OneColorSpriteManager;
import View.spriteManagers.SpriteManager;
import View.spriteManagers.StaticSpriteManager;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

/**
 * Szolgáltatásokat tartalmazó blokk, jelenleg büfé és mosdó lehetséges.
 */
public class ServiceArea extends Block implements Queueable{
    private int menuCost;
    private final ArrayDeque<Visitor> queue;
    private final ArrayDeque<Visitor> visitorsUsingThisService;
    private final ArrayList<Employee> workers;
    private final int capacity;
    private final ServiceType type;
    private int cooldownTime;
    private static final HashMap<ServiceType,SpriteManager> spriteManagerMap=new HashMap<>();

    /**
     * A szolgáltatás típusát megadva egy olyan konstruált példányt kapunk, melynek be van állítva minden adattagja.
     * @param type a szolgáltatás típusa.
     * @param pos a pozíciója.
     */
    public ServiceArea(ServiceType type, Position pos) {
        this.type=type;
        if(type==ServiceType.BUFFET)
        {
            this.pos=pos;
            buildingTime = 30;
            buildingCost = 100;
            upkeepCost = 10;
            popularityIncrease = 1.0;
            state = BlockState.UNDER_CONSTRUCTION;
            this.menuCost = 15;
            this.capacity = 2;
            this.size=new Position(2,1,false);
            workers = new ArrayList<>();
            queue = new ArrayDeque<>();
            visitorsUsingThisService = new ArrayDeque<>(capacity);
            this.cooldownTime=5;
        }
        else if(type==ServiceType.TOILET)
        {
            this.pos=pos;
            buildingTime = 30;
            buildingCost = 75;
            upkeepCost = 10;
            popularityIncrease = 1.0;
            state = BlockState.UNDER_CONSTRUCTION;
            this.menuCost = 3;
            this.capacity = 3;
            this.size=new Position(1,3,false);
            workers = new ArrayList<>();
            queue = new ArrayDeque<>();
            visitorsUsingThisService = new ArrayDeque<>(capacity);
            this.cooldownTime=2;
        }
        else throw new RuntimeException("Invalid type of service!");
    }

    /**
     * Munkás hozzáadása, menüből használjuk.
     * @param o munkás.
     */
    public void addWorker(Caterer o){workers.add(o);}

    /**
     * Látogató hozzáadása.
     * @param v a látogató
     */
    public void addVisitor(Visitor v){queue.add(v);}

    @Override
    public Color getColor() {
        return Color.blue;
    }

    public ServiceType getType() {
        return type;
    }

    public int getTicketCost() {
        return menuCost;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * @return azon látogatókat, akik éppen itt tesznek-vesznek.
     */
    public ArrayDeque<Visitor> getVisitorsUsingThisService() {
        return visitorsUsingThisService;
    }

    public int getMenuCost() { return menuCost; }

    public void setMenuCost(int menuCost) { this.menuCost = menuCost; }

    public ArrayList<Employee> getWorkers() { return workers; }

    public void setCooldownTime(int cooldownTime) {this.cooldownTime = cooldownTime; }

    public int getCooldownTime() {
        if(workers.size()==0 || getType()==ServiceType.TOILET){return cooldownTime;
        }else{
            return cooldownTime/workers.size();
        }
    }

    /**
     * Típustól függően a szolgáltatás végrehajtása. Pl. kiszolgálás.
     */
    private void startService(){
        Visitor v = queue.pollFirst();
        visitorsUsingThisService.add(v);
        if(getType()==ServiceType.TOILET){
            v.startToilet();
        }else if(getType()==ServiceType.BUFFET){
            v.startEating();
        }
        currentActivityTime=getCooldownTime();
        setState(BlockState.USED);
    }

    /**
     * A szolgáltatás megállítása. Pl. kiszolgáltuk a vevőt.
     */
    private void serviceFinished(){
        Visitor v=visitorsUsingThisService.pollFirst();
        if(getType()==ServiceType.TOILET){
            v.finishedToilet();
        }else if(getType()==ServiceType.BUFFET){
            v.finishedEating();
        }
        setState(BlockState.FREE);
    }

    /**
     * @return Egy szolgáltatás mindig használható, kivéve akkor, ha büfé, és nincsenek benne dolgozók.
     */
    private boolean isOperable(){
        if(getType()==ServiceType.BUFFET && workers.size()==0){return false;}
        return true;
    }

    /**
     * A szolgátatási terület állapotát, statisztikát állító metódus, ami minden másodpercben meghívódik a GameEngine által.
     * @param minutesPerSecond ennyi perc telik el egy másodperc alatt ( idősebességfüggő)
     */
    @Override
    public void roundHasPassed(int minutesPerSecond)
    {
        decreaseCurrentActivityTime(minutesPerSecond);

        //finished activity
        if(getState()==BlockState.UNDER_CONSTRUCTION && currentActivityTime==0){setState(BlockState.FREE);}
        if(getState()==BlockState.UNDER_REPAIR && currentActivityTime==0){repairFinished();}
        if(getState()==BlockState.NOT_OPERABLE && isOperable() && !needRepair()){setState(BlockState.FREE);}
        if (getState()==BlockState.USED && currentActivityTime==0){serviceFinished();}

        //need to start activity
        if(getState()==BlockState.FREE && needRepair()){setState(BlockState.NOT_OPERABLE);}
        if(getState()==BlockState.FREE && !isOperable()){setState(BlockState.NOT_OPERABLE);}

        if (getState()==BlockState.FREE && isOperable() && !needRepair() && queue.size()>0 && visitorsUsingThisService.size()<capacity){startService();}
    }

    /**
     * Új munkás felvétele.
     * @param r "konyhásnéni"
     * @param pg a park, ahova hozzá szeretnénk adni.
     */
    public void hire(Caterer r , Playground pg){
        if(getType() != ServiceType.BUFFET){throw new IllegalStateException("Only buffets can hire caterers!");}
        pg.hire(r);
        addWorker(r);
        r.setPosition(this.getPos());
    }

    @Override
    public String toString() {
        return "ServiceArea{" + type + " " +
                "menuCost=" + menuCost +
                ", queue=" + queue +
                ", workers=" + workers +
                ", capacity=" + capacity +
                " " + super.toString();
    }

    @Override
    public String getName()
    {
        switch(this.type)
        {
            case BUFFET:return "Buffet";
            case TOILET: return "Toilet";
            default : return "undefined";
        }
    }

    /**
     * Megkeresi a típusnak megfelelő spriteot, és be is állítja azt.
     */
    private void setupSprites(){
        if(spriteManagerMap.containsKey(type)){return;}
        switch (this.type) {
            case TOILET:
                spriteManagerMap.putIfAbsent(type,new StaticSpriteManager("graphics/toilet.png",size));
                break;
            case BUFFET:
                spriteManagerMap.putIfAbsent(type,new StaticSpriteManager("graphics/buffet.png",size));
            default:
                spriteManagerMap.putIfAbsent(type,new OneColorSpriteManager(getColor(),getSize()));break;
        }
    }

    @Override
    protected SpriteManager getSpriteManager() {
        setupSprites();
        return spriteManagerMap.get(type);
    }

    @Override
    public Queue<Visitor> getQueue() {
        return queue;
    }
}
