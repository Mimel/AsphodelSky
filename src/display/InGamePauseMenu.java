package display;

import javax.swing.*;
import java.awt.*;

public class InGamePauseMenu extends JPanel {

    public InGamePauseMenu(int winWidth, int winHeight) {
        this.setBounds(0, 0, winWidth, winHeight);
        this.setOpaque(false);
        this.setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("Please draw.");
        g.setColor(Color.MAGENTA);
        g.fillRect(400, 400, 400, 400);
    }

    public void showPauseMenu() {
        this.setVisible(true);
    }

    public void hidePauseMenu() {
        this.setVisible(false);
    }
}
