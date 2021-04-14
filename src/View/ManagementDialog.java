package View;

import Model.GameEngine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ManagementDialog extends JDialog {

    public ManagementDialog(Frame owner, GameEngine engine) {
        super(owner);
        setTitle("Management Dialog");
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel salaryTitle=new JLabel("Salaries");
        //salaryTitle.setFont(salaryTitle.getFont().deriveFont(Font.ITALIC));
        mainPanel.add(salaryTitle);
        mainPanel.add(new JLabel("Caterer salaries: "+engine.getCatererSalaries()));
        mainPanel.add(new JLabel("Operator salaries: "+engine.getOperatorSalaries()));
        mainPanel.add(new JLabel("Repairman salaries: "+engine.getRepairmanSalaries()));
        mainPanel.add(new JLabel("Cleaner salaries: "+engine.getClenanerSalaries()));

        mainPanel.add(new JSeparator());
        JLabel statTitle=new JLabel("\nStatistics");
        mainPanel.add(statTitle);
        mainPanel.add(new JLabel("Number of games: "+engine.getPg().getBuildedGameList().size()));
        mainPanel.add(new JLabel("Number of service areas: "+engine.getPg().getBuildedServiceList().size()));
        mainPanel.add(new JLabel("Number of employees: "+engine.getPg().getEmployees().size()));



        add(mainPanel);
        pack();
    }


}
