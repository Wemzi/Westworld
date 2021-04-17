package View;

import Model.Blocks.*;
import Model.GameEngine;
import Model.People.Caterer;
import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Repairman;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.*;

public class BlockInfoDialog extends JDialog implements LiveDataPanel{
    private JPanel mainPanel;
    private MainWindow2 owner;
    private Block block;

    public BlockInfoDialog(MainWindow2 owner, Block block) {
        super(owner, block.getName());
        this.owner=owner;
        this.block=block;

        mainPanel=new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        createContent(mainPanel,block,owner.engine);


        add(mainPanel);
        pack();
    }

    private static ArrayList<String> getData(Block block,GameEngine engine){
        ArrayList<String> retList=new ArrayList<>(getUnifiedBlockInfoContent(block));
        if(block instanceof Game){
            retList.add("Type: "+((Game) block).type);
            retList.add("Ticket cost: "+((Game) block).getTicketCost());
            retList.add("Queue size: "+Objects.requireNonNullElse(((Game) block).getQueue(),new ArrayList<>()).size());
            retList.add("Playing visitors: "+Objects.requireNonNullElse(((Game) block).getPlayingVisitors(),new ArrayList<>()).size());
            retList.add("Capacity: "+((Game) block).getCapacity());
            retList.add("Cooldown time: "+((Game) block).getCooldownTime());
            retList.add("Workers: "+Objects.requireNonNullElse(((Game) block).getWorkers(),new ArrayList<>()).size());

        }else if(block instanceof ServiceArea){
            retList.add("Type: "+((ServiceArea) block).getType());
            retList.add("Ticket cost: "+((ServiceArea) block).getTicketCost());
            retList.add("Queue size: "+Objects.requireNonNullElse(((ServiceArea) block).getQueue(),new ArrayList<>()).size());
            retList.add("Capacity: "+((ServiceArea) block).getCapacity());
            retList.add("Cooldown time: "+((ServiceArea) block).getCooldownTime());
            retList.add("Workers: "+Objects.requireNonNullElse(((ServiceArea) block).getWorkers(),new ArrayList<>()).size());

        }else if(block instanceof Decoration){
            retList.add("Type: "+((Decoration) block).getDecorationType());
        }else if(block instanceof Road){
            retList.add("Has garbage can: "+((Road) block).isHasGarbageCan());
            retList.add("Entrance: "+((Road) block).isEntrance());
            retList.add("Garbage: "+((Road) block).getGarbage());
            retList.add("Garbage level: "+((Road) block).getGarbageLevel());
        }
        return retList;
    }

    private static void createContent(JPanel panel, Block block,GameEngine engine){
        for(String str : getData(block,engine)){
            panel.add(new JLabel(str));
        }

        if(block instanceof ServiceArea && ((ServiceArea) block).getType()==ServiceType.BUFFET){panel.add(addCatererModificationRow((ServiceArea) block,engine));}
        if(block instanceof EmployeeBase){
            panel.add(createEmployeePanel("Cleaner",engine,new Cleaner(block.getPos(),10)));
            //panel.add(createEmployeePanel("Operator",engine,new Operator(block.getPos(),10,null)));
            panel.add(createEmployeePanel("Repairman",engine,new Repairman(block.getPos(),10)));
        }
    }

    private static LinkedList<String> getUnifiedBlockInfoContent(Block block){
        LinkedList<String> panel= new LinkedList<>();

        panel.add("Class: "+block.getName());
        panel.add("State: "+block.getState());
        panel.add("Building cost: "+block.getBuildingCost());
        panel.add("Upkeep cost: "+block.getUpkeepCost());
        panel.add("Size: "+block.getSize());
        panel.add("Position: "+block.getPos());
        panel.add("Popularity increase: "+block.getPopularityIncrease());
        panel.add("Current activity time: "+block.getCurrentActivityTime());

        return panel;
    }

    private static JPanel addCatererModificationRow(ServiceArea block,GameEngine engine){
        JPanel rowPanel=new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel,BoxLayout.LINE_AXIS));

        JLabel label=new JLabel("Caterer: "+block.getWorkers().size());
        label.setBorder(new EmptyBorder(10,10,20,20));
        rowPanel.add(label);
        JButton minusButton=new JButton("-");
        JButton plusButton=new JButton("+");

        plusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.getPg().hire(new Caterer(block.getPos(),0,block));
                label.setText("Caterer: "+block.getWorkers().size());
            }
        });

        minusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(block.getWorkers().size()==0){
                    System.out.println("Nem lehet negatív");
                    return;
                }
                engine.getPg().fire(block.getWorkers().get(0));
                label.setText("Caterer: "+block.getWorkers().size());
            }
        });

        rowPanel.add(minusButton);
        rowPanel.add(plusButton);
        return rowPanel;
    }

    private static JPanel createEmployeePanel(String name, GameEngine engine, Employee employee){
        JPanel rowPanel=new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel,BoxLayout.LINE_AXIS));

        JLabel label=new JLabel(name+": "+engine.getPg().getEmployeesLike(employee).size());
        label.setBorder(new EmptyBorder(10,10,20,20));
        rowPanel.add(label);
        JButton minusButton=new JButton("-");
        JButton plusButton=new JButton("+");

        plusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.getPg().hire(employee);
                label.setText(name+": "+engine.getPg().getEmployeesLike(employee).size());
            }
        });

        minusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.getPg().fire(employee);
                label.setText(name+": "+engine.getPg().getEmployeesLike(employee).size());
            }
        });

        rowPanel.add(minusButton);
        rowPanel.add(plusButton);
        return rowPanel;
    }

    @Override
    public void refreshData() {
        List<Component> l = Arrays.asList(mainPanel.getComponents());
        ArrayList<String> data=getData(block,owner.engine);
        for(int i=0;i<l.size() && i<data.size();i++){
            ((JLabel) l.get(i)).setText(data.get(i));
        }
        mainPanel.validate();
    }
}
