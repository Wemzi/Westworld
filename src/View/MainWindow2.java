
package View;

import Model.Blocks.Block;
import Model.Blocks.BlockState;
import Model.Blocks.FreePlace;
import Model.Blocks.Road;
import Model.GameEngine;
import Model.People.Person;
import Model.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

/**
 *
 * @author Gabor
 */
public class MainWindow2 extends JFrame{
    private static int BOX_SIZE=40;//hany pixel szeles legyen egy elem a matrixban
    public static final int NUM_OF_COLS =25;//oszlopok szama
    public static final int NUM_OF_ROWS =12;//sorok szama
    public static final int FPS=50;

    private final GameField field;
    public final GameEngine engine;

    private boolean isPlaceSelectionMode=false;
    private Block toBuild;
    private boolean isShowInfoMode=true;
    private final LinkedList<LiveDataPanel> liveDataPanels;

    private final Timer timer;

    //unused
    private final JLabel timerText;
    private final JLabel moneyLabel;
    private final JLabel popularityLabel;
    private final JLabel visitorsLabel;
    private final JButton startDayButton;

    
    public MainWindow2() {
        setBoxSize();
        engine=new GameEngine();
        field=new GameField(engine);
        liveDataPanels=new LinkedList<LiveDataPanel>();
        timer = getTimer();


        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No look and feel");
        }

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
        final MainWindow2 owner=this;
        JMenu managementMenuItem = new JMenu("Management");
        managementMenuItem.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ManagementDialog managementDialog=new ManagementDialog(owner,engine);
                managementDialog.setVisible(true);
                super.mouseClicked(e);
            }

        });


        JMenu menuToggleFullscreen = new JMenu("Fullscreen");
        menuToggleFullscreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                setUndecorated(!isUndecorated());
                setVisible(true);
            }
        });


        JMenu menuGameExit = new JMenu("Exit");
        menuGameExit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int result = JOptionPane.showConfirmDialog(owner,"Sure? You want to exit?", "Exit dialog",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if(result == JOptionPane.YES_OPTION){
                    System.exit(0);
                }else{
                    transferFocus();
                }
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
        JMenuItem timeTenMenuItem = new JMenuItem("Time 10x");
        timeTenMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.setTimerSpeed(GameEngine.TIME_1x*10);
            }
        });

        //final initialization moves
        //menu
        timeMenu.add(timeOneMenuItem);
        timeMenu.add(timeTwoMenuItem);
        timeMenu.add(timeThreeMenuItem);
        timeMenu.add(timeTenMenuItem);

        menuBar.add(buildMenu);
        menuBar.add(timeMenu);
        menuBar.add(menuToggleFullscreen);
        menuBar.add(managementMenuItem);
        menuBar.add(menuGameExit);
        setJMenuBar(menuBar);

        //window
        setLayout(new BorderLayout());
        setTitle("WestWorld");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*
        pack();
        setResizable(false);*/
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        timerText=new JLabel("Date");
        setUndecorated(true);
        setVisible(true);

        /*
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                System.out.println("Resized to " + e.getComponent().getSize());
                setBoxSize(e.getComponent().getSize());
                super.componentResized(e);
            }
        });*/
        //setBoxSize(getSize());
        field.repaint();
        startGame();
    }



    private void setBoxSize(){
        /*
        Dimension screen=new Dimension(getToolkit().getScreenSize().width,getToolkit().getScreenSize().height);
        System.out.println("resizing based:" + screen);
        BOX_SIZE=(int) Math.floor(0.8*Math.min(screen.getWidth()/NUM_OF_COLS,screen.getHeight()/NUM_OF_ROWS));
        //BOX_SIZE=40;
        System.out.println("Box size: " + BOX_SIZE);*/
    }

    public static int getBoxSize() {
        return BOX_SIZE;
    }

    private Timer getTimer() {
        final Timer timer;
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
                liveDataPanels.forEach(LiveDataPanel::refreshData);

                field.repaint();
            }
        });
        return timer;
    }


    public void startGame(){

        JPanel upperDataPanel=new JPanel(new BorderLayout());
        JPanel centerStatPanel = new JPanel();
        upperDataPanel.add(timerText,BorderLayout.WEST);
        centerStatPanel.add(moneyLabel);
        centerStatPanel.add(popularityLabel );
        centerStatPanel.add(visitorsLabel);
        centerStatPanel.add(startDayButton);
        upperDataPanel.add(centerStatPanel,BorderLayout.CENTER);
        this.add(upperDataPanel,BorderLayout.NORTH);

        JPanel gridPanel=new JPanel();
        gridPanel.setLayout(new GridBagLayout());
        gridPanel.add(field);
        field.setPreferredSize(new Dimension(BOX_SIZE* NUM_OF_COLS,BOX_SIZE* NUM_OF_ROWS));
        this.add(gridPanel,BorderLayout.CENTER);

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
        if(isShowInfoMode && !isPlaceSelectionMode && MouseEvent.BUTTON1==e.getButton()) {
            Block selectedBlock = engine.getPg().blocks[clickedHere.getX_asIndex()][clickedHere.getY_asIndex()];
            if (selectedBlock != null) {
                BlockInfoDialog blockInfoDialog = new BlockInfoDialog(this, selectedBlock);
                blockInfoDialog.setVisible(true);
                liveDataPanels.add(blockInfoDialog);
                blockInfoDialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        liveDataPanels.remove(blockInfoDialog);
                    }
                });
            } else {
                System.out.println("Block is null");
            }
        }else if(isShowInfoMode && !isPlaceSelectionMode && MouseEvent.BUTTON3==e.getButton()){
            for(Person person : engine.getPg().getPeople()){
                if(person.getPosition().getX_asIndex() == clickedHere.getX_asIndex() &&
                        person.getPosition().getY_asIndex() == clickedHere.getY_asIndex()
                ){
                    VisitorInfoDialog infoDialog= new VisitorInfoDialog(this,person);
                    infoDialog.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            super.windowClosing(e);
                            liveDataPanels.remove(infoDialog);
                        }
                    });
                    liveDataPanels.add(infoDialog);
                    infoDialog.setVisible(true);
                    //System.out.println(v.getPosition().getX_asIndex() + " "+ v.getPosition().getY_asIndex());
                }
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
