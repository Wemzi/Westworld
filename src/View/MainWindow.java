package View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.border.EmptyBorder;
/*
import progtechbeadando2.Model.Field;
import progtechbeadando2.Model.GameModel;
import progtechbeadando2.Model.Player;*/

/**
 *
 * @author Gabor
 */
public class MainWindow extends JFrame {
    private static final int DEFAULT_TABLE_SIZE =30;
    private static final int DEFAULT_CELL_SIZE =15;

    //GameModel model;
    //JButton buttons[][];
    JPanel[][] panels;
    JLabel[] playerLabels;
    JPanel table;
    JPanel playerPanel;
    private int tableSize;//dont modify!

    public MainWindow() {
        super("4-es játék");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        table = new JPanel();
        playerPanel = new JPanel();
        playerLabels = new JLabel[2];

        //menu
        JMenuBar menuBar = new JMenuBar();
        JMenu m = new JMenu("Játék");
        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem easy = new JRadioButtonMenuItem();
        easy.setText("3x3");
        easy.setMnemonic(3);
        easy.setSelected(true);
        easy.addMouseListener(new MouseAdapter(){});
        group.add(easy);

        JRadioButtonMenuItem medium = new JRadioButtonMenuItem();
        medium.setText("5x5");
        medium.setMnemonic(5);
        group.add(medium);

        JRadioButtonMenuItem hard = new JRadioButtonMenuItem();
        hard.setMnemonic(7);
        hard.setText("7x7");
        group.add(hard);

        JMenuItem startNewGameMenuItem = new JMenuItem(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newGame(group.getSelection().getMnemonic());
            }
        });
        startNewGameMenuItem.setText("Új játék");

        //game
        newGame(DEFAULT_TABLE_SIZE);

        m.add(startNewGameMenuItem);
        m.addSeparator();
        m.add(easy);
        m.add(medium);
        m.add(hard);
        menuBar.add(m);
        setJMenuBar(menuBar);
        setLayout(new BorderLayout());
        add(playerPanel,BorderLayout.NORTH);
        add(table,BorderLayout.SOUTH);
        pack();
    }
/*
    public void refreshButtons(Field[][] fields) {
        for (int i = 0; i < tableSize; ++i) {
            for (int j = 0; j < tableSize; ++j) {
                buttons[i][j].setText(String.valueOf(fields[i][j].value));
                if (fields[i][j].owner != null) {
                    buttons[i][j].setBackground(fields[i][j].owner.getColor());
                }
            }
        }
    }

    public void refreshPlayers(List<Player> players) {
        for (int i = 0; i < playerLabels.length; i++) {
            playerLabels[i].setText(players.get(i).getName() + ": " + String.valueOf(players.get(i).getPoints()) + " points");
            if (players.get(i).isActive()) {
                playerLabels[i].setForeground(players.get(i).getColor());
            } else {
                playerLabels[i].setForeground(new JLabel().getForeground());
            }
        }
    }
*/
    public void generateTable() {
        table.removeAll();
        playerPanel.removeAll();
        JLabel l=new JLabel("Money: $0");
        JLabel l2=new JLabel("Popularity: 100");
        l.setBorder(new EmptyBorder(10, 0, 10, 10));
        l2.setBorder(new EmptyBorder(10, 10, 10, 0));
        playerLabels[0] = l;
        playerLabels[1] = l2;
        playerPanel.add(playerLabels[0]);
        playerPanel.add(playerLabels[1]);
        playerPanel.validate();

        table.validate();
    }

    public void newGame(int size) {
        generateTable();
        tableSize = size;
        //buttons = new JButton[size][size];
        panels = new JPanel[size][size];
        table.setLayout(new GridLayout(size, size));

        //model = new GameModel(this, size);

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                //JButton b = new JButton("0");
                JPanel b = new JPanel();
                if((i+j)%2==0){b.setBackground(Color.WHITE);
                }else{b.setBackground(Color.BLACK);}
                b.setPreferredSize(new Dimension(DEFAULT_CELL_SIZE,DEFAULT_CELL_SIZE));
                b.addMouseListener(new MyMouseAdapter(j, i/*, model*/));
                //buttons[j][i] = b;
                panels[j][i] = b;
                table.add(b);
            }
        }
        //refreshPlayers(model.getPlayers());

        pack();
    }
/*
    public void showEndScreen(Player p) {
        int dialogResult = JOptionPane.showConfirmDialog(null,p.getName() +" nyert. Gratulálunk!\nSzeretne új játékot kezdeni?", "Játék vége", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            newGame(tableSize);
        }
    }

 */
}
