package View;

import Model.Blocks.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class BlockInfoDialog extends JDialog {

    public BlockInfoDialog(Frame owner, Block block) {
        super(owner, block.getName());
        JPanel mainPanel=new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20,20,20,20));

        createContent(mainPanel,block);


        add(mainPanel);
        pack();
    }

    private static void createContent(JPanel panel, Block block){
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

        }else if(block instanceof Decoration){
            panel.add(new JLabel("Type: "+((Decoration) block).getDecorationType()));
        }else if(block instanceof Road){
            panel.add(new JLabel("Has garbage can: "+((Road) block).isHasGarbageCan()));
            panel.add(new JLabel("Entrance: "+((Road) block).isEntrance()));
        }
    }

    private static void addUnifiedBlockInfoContent(JPanel panel, Block block){
        panel.add(new JLabel("State: "+block.getState()));
        panel.add(new JLabel("Building cost: "+block.getBuildingCost()));
        panel.add(new JLabel("Upkeep cost: "+block.getUpkeepCost()));
        panel.add(new JLabel("Size: "+block.getSize()));
        panel.add(new JLabel("Position: "+block.getPos()));
        panel.add(new JLabel("Popularity increase: "+block.getPopularityIncrease()));
    }
}
