package View;

import Model.Blocks.*;
import Model.Position;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuCreator {
    private final GameWindow parent;
    private final JMenu m;

    /**
     *
     * @param m A Jmenu, amiben a menü kialakításra fog kerülni
     * @param parent Az ablak, amelynek a menüjét kell létrehozni
     */
    public MenuCreator(JMenu m, GameWindow parent) {
        this.parent = parent;
        this.m = m;
    }

    /**
     * Feltölti a construktorban kapott JMenu -t almenükkel.
     */
    void inflate(){createMenus(m);}

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
                    parent.startPlaceSelectionMode(g);
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
                    parent.startPlaceSelectionMode(g);
                }
            });
        }
    }

    private void createDecorationMenuItems(JMenu menu){
        ArrayList<DecType> possibleBlocks=new ArrayList<>( Arrays.asList(DecType.values()));
        for(DecType type : possibleBlocks){
            JMenuItem gameMenuItem=new JMenuItem(type.toString());
            menu.add(gameMenuItem);
            gameMenuItem.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Decoration g=new Decoration(type,new Position(-1,-1,true));
                    parent.startPlaceSelectionMode(g);
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
        JMenuItem buildemployeeBase = new JMenuItem("Employee Base");
        buildMenu.add(buildemployeeBase);
        buildemployeeBase.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block = new EmployeeBase(new Position(0,0,false));
                parent.startPlaceSelectionMode(block);
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
                parent.startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------

        //-----------Egy menu elem kezdete ------------
        JMenuItem garbageCanMenuItem=new JMenuItem("Garbage Can");
        buildMenu.add(garbageCanMenuItem);
        garbageCanMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new GarbageCan(new Position(0,0,false));
                parent.startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------

        //-----------Egy menu elem kezdete ------------
        JMenuItem entranceMenuItem =new JMenuItem("Entrance");
        buildMenu.add(entranceMenuItem);
        entranceMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new Road(200,15,0,BlockState.UNDER_PLACEMENT,false,true,0);
                parent.startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------

        JMenu buildDecorationMenu=new JMenu("Decoration");
        createDecorationMenuItems(buildDecorationMenu);
        buildMenu.add(buildDecorationMenu);

        //-----------Egy menu elem kezdete ------------
        JMenuItem demolishMenuItem=new JMenuItem("Demolish");
        buildMenu.add(demolishMenuItem);
        demolishMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Block block=new FreePlace(0,0,0,BlockState.UNDER_PLACEMENT);
                parent.startPlaceSelectionMode(block);
            }
        });
        //------------ vege ---------------
    }
}
