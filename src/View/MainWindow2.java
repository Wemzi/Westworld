
package View;

import Model.Blocks.*;
import Model.GameEngine;
import Model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Gabor
 */
 // TODO: Ki tudjuk választani a játékok közül, hogy melyiket akarjuk megépíteni.
    // TODO: Service area
public class MainWindow2 extends JFrame{
    public static final int BOX_SIZE=50;//hany pixel szeles legyen egy elem a matrixban
    public static final int NUM_OF_ROWS=25;//sorok szama
    public static final int NUM_OF_COLS=15;//oszlopok szama
    public static final int FPS=20;

    private final GameField field;
    private final GameEngine engine;

    private boolean isPlaceSelectionMode=false;
    private MouseListener placementListener;

    private final Timer timer;

    //unused
    private final JLabel timerText;
    private final JLabel moneyLabel;
    private final JLabel popularityLabel;
    private final JLabel visitorsLabel;


    public MainWindow2() {

        engine=new GameEngine();
        field=new GameField(engine);
        timer=new Timer(1000/FPS, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moneyLabel.setText("Money: $"+engine.getPg().getMoney());
                popularityLabel.setText("Popularity: "+engine.getPg().getPopularity());
                visitorsLabel.setText("Visitors: "+0);
                timerText.setText(engine.getPg().dateToString());

                field.repaint();
            }
        });

        //handle click event
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                //System.out.println("Clicked pixel: "+e.getX()+" "+e.getY());
                Position d= new Position(e.getX(),e.getY(),true);
                System.out.println("Clicked box: "+d.getX_asIndex() +" "+d.getY_asIndex());
                Block selectedBlock=engine.getPg().blocks[d.getX_asIndex()][d.getY_asIndex()];
                onBlockClick(selectedBlock);

            }
        });

        //labels
        moneyLabel=new JLabel("Money: $0");
        popularityLabel=new JLabel("Popularity: 0");
        visitorsLabel=new JLabel("Visitors: 0");

        //menu items
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Other");

        createMenus(menuBar);


        /*
        JMenuItem buildGameMenuItem=new JMenuItem("Game");
        JMenuItem buildServiceAreaMenuItem=new JMenuItem("service");
        JMenuItem buildRoadMenuItem=new JMenuItem("road");
        JMenuItem buildDecorationMenuItem=new JMenuItem("decor");
        JMenuItem buildFreePlaceMenuItem=new JMenuItem("free");

        buildGameMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Build game");
            }
        });


        buildMenu.add(buildGameMenuItem);
        buildMenu.add(medium);
        buildMenu.add(large);
*/


        JMenuItem managementMenuItem = new JMenuItem(new AbstractAction("Management") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //final initialization moves
        //menu

        menuGame.add(managementMenuItem);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);

        menuBar.add(menuGame);
        setJMenuBar(menuBar);

        //window
        setLayout(new BorderLayout());
        timerText=new JLabel("Date");
        add(timerText);
        setTitle("WestWorld");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        startGame();
    }


    public void startGame(){

        field.setPreferredSize(new Dimension(BOX_SIZE*NUM_OF_ROWS,BOX_SIZE*NUM_OF_COLS));
        this.add(field,BorderLayout.SOUTH);
        pack();


        JPanel playersPanel=new JPanel();
        playersPanel.add(moneyLabel);
        playersPanel.add(popularityLabel);
        playersPanel.add(visitorsLabel);
        this.add(playersPanel,BorderLayout.NORTH);
        pack();

        timer.start();
        //gameloop
        //animationTimer.restart();
        //System.out.println("Timer started");
/*
        stopper=new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timerText.setText(String.valueOf(++time)+" millisecond");
            }
        });
        stopper.start();*/

    }

    private void onBlockClick(Block b){//Ezt modositsd ha valamit ki akarsz irni a blokkrol arrol a blokkrol, amire eppen rakattintottak
        if(b !=null){
            System.out.println("--------------------------------------------");
            System.out.println("Selected block:");
            System.out.println(b.toString());
            System.out.println("--------------------------------------------");
        }else{
            System.out.println("Block is null");
        }
    }

    private void createGameMenuItems(JMenu menu){
        ArrayList<GameType> possibleBlocks=new ArrayList<>( Arrays.asList(GameType.values()));
        for(GameType type : possibleBlocks){
            JMenuItem gameMenuItem=new JMenuItem(type.toString());
            menu.add(gameMenuItem);
            gameMenuItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Game g=new Game(type,new Position(-1,-1,true));
                    //Game g=new Game(new Position(1,1,false),new Position(-1,-1,true));//direkt invalid hely, még nem tudjuk hova kerul
                    g.setState(BlockState.UNDER_PLACEMENT);//elhelyezes alatt
                    startPlaceSelectionMode(g);
                }
            });
        }
    }

    private void createServiceMenuItems(JMenu menu){
        ArrayList<ServiceType> possibleBlocks=new ArrayList<>( Arrays.asList(ServiceType.values()));
        for(ServiceType type : possibleBlocks){
            JMenuItem gameMenuItem=new JMenuItem(type.toString());
            menu.add(gameMenuItem);
            gameMenuItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ServiceArea g=new ServiceArea(type,new Position(-1,-1,true));
                    startPlaceSelectionMode(g);
                }
            });
        }
    }


    private void createMenus(JMenuBar buildMenu){
        JMenu buildGameMenu=new JMenu("Game");
        createGameMenuItems(buildGameMenu);
        buildMenu.add(buildGameMenu);

        JMenu buildServiceMenu=new JMenu("Service");
        createServiceMenuItems(buildServiceMenu);
        buildMenu.add(buildServiceMenu);

        //-----------Egy menu elem kezdete ------------
        JMenuItem roadMenuItem=new JMenuItem("Road");
        buildMenu.add(roadMenuItem);
        roadMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new Road(100,5,0,BlockState.UNDER_PLACEMENT,false,false,0);
                startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------

        //-----------Egy menu elem kezdete ------------
        JMenuItem decorMenuItem=new JMenuItem("Decoration");
        buildMenu.add(decorMenuItem);
        decorMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new Decoration(100,1,5,BlockState.UNDER_PLACEMENT);
                startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------

        //-----------Egy menu elem kezdete ------------
        JMenuItem demolishMenuItem=new JMenuItem("Demolish");
        buildMenu.add(demolishMenuItem);
        demolishMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new FreePlace(0,0,0,BlockState.UNDER_PLACEMENT);
                startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------
    }

    private boolean buildBlock(Block b){
        if(b instanceof FreePlace) {
            engine.demolish((FreePlace) b);
            return true;
        }
        boolean l=engine.buildBlock(b);
        if(l){
            field.repaint();
        }else{
            System.out.println("Foglalt!");
        }
        return l;
    }

    private void stopPlaceSelectionMode(){
        if(!isPlaceSelectionMode){
            System.err.println("Place selection mode is already inactive");
        }else{
            isPlaceSelectionMode=false;
            field.removeMouseListener(placementListener);
            field.disableMouseFollowing();
        }
    }


    private boolean startPlaceSelectionMode(Block toBuild){
        if(isPlaceSelectionMode){
            System.err.println("Already in place selection mode");
            return false;
        }else{
            isPlaceSelectionMode=true;
            field.enableMouseFollowing(toBuild);
            System.out.println("Select a place");
            placementListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if(MouseEvent.BUTTON1==e.getButton()){//Left-click
                        Position clickedHere=new Position(e.getX(),e.getY(),true);
                        //toBuild.pos=new Position(clickedHere.getX_asIndex(),clickedHere.getY_asIndex(),false);//beigazitja egy negyzetbe/dobozba, h ne random pixelen kezdodjon
                        toBuild.pos=Position.useMagicGravity(clickedHere);//beigazitja egy negyzetbe/dobozba, h ne random pixelen kezdodjon
                        if(toBuild instanceof FreePlace){
                            toBuild.size= engine.getPg().blocks[toBuild.pos.getX_asIndex()][toBuild.pos.getY_asIndex()].getSize();
                            toBuild.pos=engine.getPg().blocks[toBuild.pos.getX_asIndex()][toBuild.pos.getY_asIndex()].getPos();
                        }
                        buildBlock(toBuild);
                        //System.out.println("Block placed");
                        stopPlaceSelectionMode();
                    }else if(MouseEvent.BUTTON3==e.getButton()){//right-click
                        System.out.println("Cancelled with mouse right click");
                        stopPlaceSelectionMode();
                    }

                }
            };

            field.addMouseListener(placementListener);
        }
        return true;
    }

}
