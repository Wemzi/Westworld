
package View;

import Model.Blocks.*;
import Model.Coord;
import Model.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Gabor
 */
public class MainWindow2 extends JFrame{
    public static final int BOX_SIZE=20;//hany pixel szeles legyen egy elem a matrixban
    public static final int NUM_OF_ROWS=25;//sorok szama
    public static final int NUM_OF_COLS=25;//oszlopok szama
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
    private int time;


    public MainWindow2() {

        engine=new GameEngine();
        field=new GameField(engine);
        timer=new Timer(1000/FPS, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moneyLabel.setText("Money: $"+Integer.toString(engine.getPg().getMoney()));
                popularityLabel.setText("Popularity: "+Double.toString(engine.getPg().getPopularity()));
                timerText.setText(engine.getDate());

                //field.repaint();
            }
        });

        //handle click event
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                //System.out.println("Clicked pixel: "+e.getX()+" "+e.getY());
                IndexPair d= coordToIndexPair(e.getX(),e.getY());
                System.out.println("Clicked box: "+d.i +" "+d.j);
                Block selectedBlock=engine.getPg().blocks[d.i][d.j];
                onBlockClick(selectedBlock);

            }
        });

        //labels
        moneyLabel=new JLabel("Money: $0");
        //l1.setForeground(Color.green);
        popularityLabel=new JLabel("Popularity: 0");

        //menu items
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Other");
        JMenu buildMenu=new JMenu("Build");

        //createMenuItems(buildMenu);
        createMenuItems(menuBar);


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
        JMenuItem demolishMenuItem = new JMenuItem(new AbstractAction("Demolish") {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

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

        menuGame.add(demolishMenuItem);
        menuGame.add(managementMenuItem);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);

        menuBar.add(menuGame);
        //menuBar.add(buildMenu);
        setJMenuBar(menuBar);

        //window
        setLayout(new BorderLayout());
        timerText=new JLabel("2021.20.21");
        time=0;
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
            System.out.println("Color of selected block:");
            System.out.println(b.getColor());
            System.out.println("--------------------------------------------");
        }else{
            System.out.println("Block is null");
        }
    }


    private void createMenuItems(JMenuBar buildMenu){

        //-----------Egy menu elem kezdete ------------
        JMenuItem gameMenuItem=new JMenuItem("Game");
        buildMenu.add(gameMenuItem);
        gameMenuItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Game g=new Game(new IndexPair(1,1),MainWindow2.indexPairToCoord(-1,-1));//direkt invalid hely, még nem tudjuk hova kerul
                    g.setState(BlockState.UNDER_PLACEMENT);//elhelyezes alatt //todo egy konstruktor ami ezt es a pos-t -1-1re alapbol beallitja
                    startPlaceSelectionMode(g);
                }
        });
        //------------ vege ---------------

        //Az alabbit copy pasteld, ha tobbfele menutitem kell
        //-----------Egy menu elem kezdete ------------
        JMenuItem serviceMenuItem=new JMenuItem("Service");
        buildMenu.add(serviceMenuItem);
        serviceMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new ServiceArea(100,5,1,BlockState.UNDER_PLACEMENT,100,20);//direkt invalid hely, még nem tudjuk hova kerul
                startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------

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
    }

    private boolean buildBlock(Block b){
        engine.buildBlock(b);
        field.repaint();
        return true;//todo Ellenorzes, hogy lerakhato-e
    }

    private void stopPlaceSelectionMode(){
        if(!isPlaceSelectionMode){
            System.err.println("Place selection mode is already inactive");
        }else{
            isPlaceSelectionMode=false;
            field.removeMouseListener(placementListener);
        }
    }


    private boolean startPlaceSelectionMode(Block toBuild){
        if(isPlaceSelectionMode){
            System.err.println("Already in place selection mode");
            return false;
        }else{
            isPlaceSelectionMode=true;
            System.out.println("Select a place");
            placementListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    Coord clickedHere=new Coord(e.getX(),e.getY());
                    toBuild.pos=indexPairToCoord(coordToIndexPair(clickedHere));//beigazitja egy negyzetbe/dobozba, h ne random pixelen kezdodjon
                    buildBlock(toBuild);
                    System.out.println("------- Block placed-----------");
                    stopPlaceSelectionMode();
                }

            };

            field.addMouseListener(placementListener);
        }
        return true;
    }


    //Conversions
    public static IndexPair coordToIndexPair(int x, int y){
        int i=x/BOX_SIZE;
        int j=y/BOX_SIZE;
        return new IndexPair(i,j);
    }
    public static IndexPair coordToIndexPair(Coord c){
        int i=c.posX/BOX_SIZE;
        int j=c.posY/BOX_SIZE;
        return new IndexPair(i,j);
    }
    public static int coordToIndex(int coord){return coord/BOX_SIZE;}

    public static Coord indexPairToCoord(IndexPair pair){
        int i=pair.i *BOX_SIZE;
        int j=pair.j *BOX_SIZE;
        return new Coord(i,j);
    }
    public static Coord indexPairToCoord(int i, int j){
        return new Coord(i*BOX_SIZE,j*BOX_SIZE);
    }
    public static int indexToCoord (int index){return index*BOX_SIZE;}

    /*
    private void showEndScreen(){
        Player winner=currentPlayer;
        winner.points++;
        database.saveToDatabase(winner);
        //System.out.println("loser:"+getOtherPlayer(currentPlayer));
        //System.out.println("winner:"+winner);
        int dialogResult = JOptionPane.showConfirmDialog(null,"The winner is "+winner.name +"!\nCongratulations!\nDo you want to start a new game?", "End", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            startGame(p1,p2);
        }
    }*/

}
