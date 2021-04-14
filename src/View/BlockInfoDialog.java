package View;

import Model.Blocks.*;
import Model.GameEngine;
import Model.People.Caterer;
import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Repairman;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Objects;

public class BlockInfoDialog extends JDialog {

    public BlockInfoDialog(MainWindow2 owner, Block block) {
        super(owner, block.getName());
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        createContent(mainPanel,block,owner.engine);


        add(mainPanel);
        pack();
    }

    private static void createContent(JPanel panel, Block block,GameEngine engine){
        addUnifiedBlockInfoContent(panel,block);
        if(block instanceof Game){

            panel.add(new JLabel("Type: "+((Game) block).type));
            panel.add(new JLabel("Ticket cost: "+((Game) block).getTicketCost()));
            panel.add(new JLabel("Queue size: "+Objects.requireNonNullElse(((Game) block).getQueue(),new ArrayList<>()).size()));
            panel.add(new JLabel("Capacity: "+((Game) block).getCapacity()));
            panel.add(new JLabel("Cooldown time: "+((Game) block).getCooldownTime()));
            panel.add(new JLabel("Workers: "+Objects.requireNonNullElse(((Game) block).getWorkers(),new ArrayList<>()).size()));

        }else if(block instanceof ServiceArea){
            panel.add(new JLabel("Type: "+((ServiceArea) block).getType()));
            panel.add(new JLabel("Ticket cost: "+((ServiceArea) block).getTicketCost()));
            panel.add(new JLabel("Queue size: "+Objects.requireNonNullElse(((ServiceArea) block).getQueue(),new ArrayList<>()).size()));
            panel.add(new JLabel("Capacity: "+((ServiceArea) block).getCapacity()));
            panel.add(new JLabel("Cooldown time: "+((ServiceArea) block).getCooldownTime()));
            panel.add(new JLabel("Workers: "+Objects.requireNonNullElse(((ServiceArea) block).getWorkers(),new ArrayList<>()).size()));

            if(((ServiceArea) block).getType()==ServiceType.BUFFET){panel.add(addCatererModificationRow((ServiceArea) block,engine));}
        }else if(block instanceof Decoration){
            panel.add(new JLabel("Type: "+((Decoration) block).getDecorationType()));
        }else if(block instanceof Road){
            panel.add(new JLabel("Has garbage can: "+((Road) block).isHasGarbageCan()));
            panel.add(new JLabel("Entrance: "+((Road) block).isEntrance()));
            panel.add(new JLabel("Garbage: "+((Road) block).getGarbage()));
            panel.add(new JLabel("Garbage level: "+((Road) block).getGarbageLevel()));
        }else if(block instanceof EmployeeBase){
            panel.add(createEmployeePanel("Cleaner",engine,new Cleaner(block.getPos(),10)));
            //panel.add(createEmployeePanel("Operator",engine,new Operator(block.getPos(),10,null)));
            panel.add(createEmployeePanel("Repairman",engine,new Repairman(block.getPos(),10)));
        }
    }

    private static void addUnifiedBlockInfoContent(JPanel panel, Block block){
        panel.add(new JLabel("Class: "+block.getName()));
        panel.add(new JLabel("State: "+block.getState()));
        panel.add(new JLabel("Building cost: "+block.getBuildingCost()));
        panel.add(new JLabel("Upkeep cost: "+block.getUpkeepCost()));
        panel.add(new JLabel("Size: "+block.getSize()));
        panel.add(new JLabel("Position: "+block.getPos()));
        panel.add(new JLabel("Popularity increase: "+block.getPopularityIncrease()));
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
}
