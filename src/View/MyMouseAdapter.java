package View;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
// progtechbeadando2.Model.GameModel;

/**
 *
 * @author Gabor
 */
public class MyMouseAdapter extends MouseAdapter {

    private final int x;
    private final int y;
    //private final GameModel model;

    public MyMouseAdapter(int x, int y/*, GameModel model*/) {
        this.x = x;
        this.y = y;
        //this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            System.out.println("x="+x+"y="+y);
            //model.fieldClicked(x, y);
        }
    }
}
