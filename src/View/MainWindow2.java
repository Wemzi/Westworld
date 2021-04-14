
package View;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.FreePlace;
import Model.Blocks.Road;
import Model.GameEngine;
import Model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 *
 * @author Gabor
 */
public class MainWindow2 extends JFrame{
    public static final int BOX_SIZE=40;//hany pixel szeles legyen egy elem a matrixban
    public static final int NUM_OF_COLS =25;//oszlopok szama
    public static final int NUM_OF_ROWS =12;//sorok szama
    public static final int FPS=50;

    private final GameField field;
    public final GameEngine engine;

    private boolean isPlaceSelectionMode=false;
    private Block toBuild;
    private boolean isShowInfoMode=true;

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
                if(engine.isBuildingPeriod() != startDayButton.isEnabled()){
                    startDayButton.setEnabled(engine.isBuildingPeriod());
                }

                field.repaint();
            }
        });

        //handle click event
        field.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(toBuild instanceof Road){return;}
                onFieldClick(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(toBuild instanceof Road){onFieldClick(e);}
            }
        });

        field.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(isPlaceSelectionMode && toBuild instanceof Road){
                    toBuild=new Road(100,5,0, BlockState.UNDER_PLACEMENT,false,((Road) toBuild).isEntrance(),0);
                    onFieldClick(e);
                }
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
        JMenu timeMenu = new JMenu("Time");
        JMenu otherMenu = new JMenu("Other");

        new MenuCreator(buildMenu,this).inflate();

        //management dialog
        MainWindow2 owner=this;
        JMenuItem managementMenuItem = new JMenuItem("Management");
        managementMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagementDialog managementDialog=new ManagementDialog(owner,engine);
                managementDialog.setVisible(true);
            }
        });

        JMenuItem menuGameExit = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });



        JMenuItem timeOneMenuItem = new JMenuItem("Time 1x");
        timeOneMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.setTimerSpeed(GameEngine.TIME_1x);
            }
        });
        JMenuItem timeTwoMenuItem = new JMenuItem("Time 2x");
        timeTwoMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.setTimerSpeed(GameEngine.TIME_1x*2);
            }
        });
        JMenuItem timeThreeMenuItem = new JMenuItem("Time 3x");
        timeThreeMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.setTimerSpeed(GameEngine.TIME_1x*3);
            }
        });

        //final initialization moves
        //menu
        timeMenu.add(timeOneMenuItem);
        timeMenu.add(timeTwoMenuItem);
        timeMenu.add(timeThreeMenuItem);

        otherMenu.add(managementMenuItem);
        otherMenu.addSeparator();
        otherMenu.add(menuGameExit);

        menuBar.add(buildMenu);
        menuBar.add(timeMenu);
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

        JPanel playersPanel=new JPanel();
        playersPanel.add(moneyLabel);
        playersPanel.add(popularityLabel);
        playersPanel.add(visitorsLabel);
        playersPanel.add(startDayButton);
        this.add(playersPanel,BorderLayout.NORTH);
        pack();

        timer.start();

    }


    private boolean buildBlock(Block b){
        if(b instanceof FreePlace) {
            engine.demolish(b.getPos());
            field.repaint();
            return true;
        }
        boolean l=engine.buildBlock(b);
        if(l){field.repaint();
        }else if(! (toBuild instanceof Road)){
            System.out.println("Foglalt!");
        }
        return l;
    }

    private void stopPlaceSelectionMode(){
        if(!isPlaceSelectionMode){
            System.err.println("Place selection mode is already inactive");
        }else{
            isPlaceSelectionMode=false;
            isShowInfoMode=true;
            field.disableMouseFollowing();
        }
    }

    boolean startPlaceSelectionMode(Block toBuild){
        if(isPlaceSelectionMode){
            System.err.println("Already in place selection mode");
            return false;
        }else{
            isShowInfoMode=false;
            isPlaceSelectionMode=true;
            field.enableMouseFollowing(toBuild);
            this.toBuild=toBuild;
            System.out.println("Select a place");
        }
        return true;
    }

    public void onFieldClick(MouseEvent e){
        Position clickedHere=new Position(e.getX(),e.getY(),true);
        if(isShowInfoMode && !isPlaceSelectionMode && MouseEvent.BUTTON1==e.getButton()){
            Block selectedBlock=engine.getPg().blocks[clickedHere.getX_asIndex()][clickedHere.getY_asIndex()];
            if(selectedBlock !=null){
                BlockInfoDialog blockInfoDialog = new BlockInfoDialog(this,selectedBlock);
                blockInfoDialog.setVisible(true);
            }else{
                System.out.println("Block is null");
            }
        }else if(isPlaceSelectionMode){
            if(MouseEvent.BUTTON1==e.getButton() || MouseEvent.NOBUTTON == e.getButton() && toBuild instanceof Road){//Left-click
                toBuild.pos=Position.useMagicGravity(clickedHere);//beigazitja egy negyzetbe/dobozba, h ne random pixelen kezdodjon
                if(toBuild instanceof FreePlace){
                    toBuild.size= engine.getPg().blocks[toBuild.pos.getX_asIndex()][toBuild.pos.getY_asIndex()].getSize();
                    toBuild.pos=engine.getPg().blocks[toBuild.pos.getX_asIndex()][toBuild.pos.getY_asIndex()].getPos();
                }
                buildBlock(toBuild);
                if(MouseEvent.NOBUTTON != e.getButton()){stopPlaceSelectionMode();}
            }else if(MouseEvent.BUTTON3==e.getButton()){//right-click
                System.out.println("Cancelled with mouse right click");
                stopPlaceSelectionMode();
            }
        }
    }

}
