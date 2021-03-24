
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
public class MainWindow2 extends JFrame{
    public static final int BOX_SIZE=40;//hany pixel szeles legyen egy elem a matrixban
    public static final int NUM_OF_COLS =25;//oszlopok szama
    public static final int NUM_OF_ROWS =12;//sorok szama
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
    private final JButton startDayButton;


    public MainWindow2() {

        engine=new GameEngine();
        field=new GameField(engine);
        timer=new Timer(1000/FPS, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moneyLabel.setText("Money: $"+engine.getPg().getMoney());
                popularityLabel.setText("Popularity: "+engine.getPg().getPopularity());
                visitorsLabel.setText("Visitors: "+engine.getPg().getVisitors().size());
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

        //Button
        startDayButton=new JButton("Start day");
        startDayButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.startDay();
            }
        });

        //menu items
        JMenuBar menuBar = new JMenuBar();
        JMenu buildMenu = new JMenu("Build");
        JMenu otherMenu = new JMenu("Other");

        createMenus(buildMenu);



        JMenuItem managementMenuItem = new JMenuItem(new AbstractAction("Management") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //management dialog
        ManagementDialog managementDialog=new ManagementDialog(this,engine);
        managementMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                managementDialog.setVisible(true);
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

        otherMenu.add(managementMenuItem);
        otherMenu.addSeparator();
        otherMenu.add(menuGameExit);

        menuBar.add(buildMenu);
        menuBar.add(otherMenu);
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

        field.setPreferredSize(new Dimension(BOX_SIZE* NUM_OF_COLS,BOX_SIZE* NUM_OF_ROWS));
        this.add(field,BorderLayout.SOUTH);
        pack();


        JPanel playersPanel=new JPanel();
        playersPanel.add(moneyLabel);
        playersPanel.add(popularityLabel);
        playersPanel.add(visitorsLabel);
        playersPanel.add(startDayButton);
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


    private void createMenus(JMenu buildMenu){
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
        JMenuItem garbageCanMenuItem=new JMenuItem("Garbage Can");
        buildMenu.add(garbageCanMenuItem);
        garbageCanMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //todo create a garbage can block class
                Block block=new GarbageCan();
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
            engine.demolish(b);
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
