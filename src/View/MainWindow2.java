
package View;

import Model.Blocks.Block;
import Model.Coord;
import Model.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/**
 *
 * @author Gabor
 */
public class MainWindow2 extends JFrame{
    public static final int BOX_SIZE=20;//hany pixel szeles legyen egy elem a matrixban
    public static final int NUM_OF_ROWS=25;//sorok szama
    public static final int NUM_OF_COLS=25;//oszlopok szama

    private final GameField field;
    private final GameEngine engine;

    private final JLabel timerText;
    private int time;


    public MainWindow2() {
        engine=new GameEngine();
        field=new GameField(engine);

        //handle click event
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //super.mouseClicked(e);
                System.out.println("Clicked pixel: "+e.getX()+" "+e.getY());
                IndexPair d= coordToIndexPair(e.getX(),e.getY());
                System.out.println("Which is box: "+d.i +" "+d.j);
                Block selectedBlock=engine.getPg().blocks[d.i][d.j];
                if(selectedBlock !=null){
                    System.out.println("Color of selected block:");
                    System.out.println(selectedBlock.getColor());
                    System.out.println("--------------------------------------------");
                }

            }
        });

        //menu items
        JMenuBar menuBar = new JMenuBar();
        JMenu menuGame = new JMenu("Game");
        JMenu newGameMenu=new JMenu("New game");

        JMenuItem small=new JMenuItem("Small");
        JMenuItem medium=new JMenuItem("Medium");
        JMenuItem large=new JMenuItem("Large");

        small.addActionListener(new AbstractAction("New Game") {
            @Override
            public void actionPerformed(ActionEvent e) {

                startGame();
                //new Thread(() -> {startGame(null,null);}).start();

            }
        });
        medium.addActionListener(new AbstractAction("New Game") {
            @Override
            public void actionPerformed(ActionEvent e) {

                startGame();
            }
        });
        large.addActionListener(new AbstractAction("New Game") {
            @Override
            public void actionPerformed(ActionEvent e) {

                startGame();
            }
        });

        newGameMenu.add(small);
        newGameMenu.add(medium);
        newGameMenu.add(large);

        JMenuItem menuHighScores = new JMenuItem(new AbstractAction("Highscores") {
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
        menuGame.add(newGameMenu);
        menuGame.add(menuHighScores);
        menuGame.addSeparator();
        menuGame.add(menuGameExit);
        menuBar.add(menuGame);
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
        JLabel l1=new JLabel("Money: $0");
        //l1.setForeground(Color.green);
        JLabel l2=new JLabel("Popularity: 0");
        //l2.setForeground(p2.color);
        playersPanel.add(l1);
        playersPanel.add(l2);
        this.add(playersPanel,BorderLayout.NORTH);
        pack();


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


}
