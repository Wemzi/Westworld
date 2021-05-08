package View;

import Model.GameEngine;
import Model.People.Cleaner;
import Model.People.Person;
import Model.People.Visitor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Emberek adatainak megjelenítésére képes Dialog.
 * Képes az adatok folyamatos frissülését nyomon követni.
 */
public class PersonInfoDialog extends JDialog implements LiveDataPanel{
    private final JPanel mainPanel;
    private final GameWindow owner;
    private final Person person;

    /**
     *
     * @param owner - A szülő, amely kéri a Dialog megjelenítését
     * @param person - Akinek az adatait meg kell jeleníteni
     */
    public PersonInfoDialog(GameWindow owner, Person person) {
            super(owner, person.getPersonClass());
            this.owner=owner;
            this.person=person;

            mainPanel=new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.PAGE_AXIS));
            mainPanel.setBorder(new EmptyBorder(20,20,20,20));

            createContent(mainPanel,person,owner.engine);

            add(mainPanel);
            pack();
        }

        private static ArrayList<String> getData(Person p, GameEngine engine){
            ArrayList<String> retList=new ArrayList<>(getUnifiedPersonInfoContent(p));
            if( p instanceof Visitor){
                Visitor v =(Visitor)p;
                retList.add("Name:"+v.getName());
                retList.add("Happiness:"+v.getHappiness());
                retList.add("Hunger:"+v.getHunger());
                retList.add("StayingTime:"+v.getStayingTime());
                retList.add("Playfulness:"+v.getPlayfulness());
                retList.add("VisitorState:"+v.getState());
                retList.add("Current activity length:"+v.getCurrentActivityLength());
                retList.add("Goal:"+v.goal);
            }else if(p instanceof Cleaner){
                Cleaner cleaner= (Cleaner) p;
                retList.add("Name:"+cleaner.getName());
                retList.add("whatSheCleans:"+cleaner.getWhatSheCleans());
            }

            return retList;
        }

        private static void createContent(JPanel panel, Person p,GameEngine engine){
            for(String str : getData(p,engine)){
                panel.add(new JLabel(str));
            }
        }

        private static LinkedList<String> getUnifiedPersonInfoContent(Person p){
            LinkedList<String> panel= new LinkedList<>();

            panel.add("Direction: "+p.direction);
            panel.add("pathPositionIndex: "+p.pathPositionIndex);
            panel.add("isMoving: "+p.isMoving);
            panel.add("isBusy(): "+p.isBusy());
            panel.add("Position: "+p.getPosition());

            return panel;
        }


        @Override
        public void refreshData() {
            List<Component> l = Arrays.asList(mainPanel.getComponents());
            ArrayList<String> data=getData(person,owner.engine);
            for(int i=0;i<l.size() && i<data.size();i++){
                ((JLabel) l.get(i)).setText(data.get(i));
            }
            mainPanel.validate();
        }

}
