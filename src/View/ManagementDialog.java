package View;

import Model.GameEngine;
import Model.People.Cleaner;
import Model.People.Employee;
import Model.People.Operator;
import Model.People.Repairman;
import Model.Position;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ManagementDialog extends JDialog {

    public ManagementDialog(Frame owner, GameEngine engine) {
        super(owner);
        setTitle("Management Dialog");
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));


        //mainPanel.add(createEmployeePanel("Caterer",engine,new Caterer(new Position(0,0,false),10,null)));
        mainPanel.add(createEmployeePanel("Cleaner",engine,new Cleaner(new Position(0,0,false),10)));
        mainPanel.add(createEmployeePanel("Operator",engine,new Operator(new Position(0,0,false),10,null)));
        mainPanel.add(createEmployeePanel("Repairman",engine,new Repairman(new Position(0,0,false),10)));

        add(mainPanel);
        pack();
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
