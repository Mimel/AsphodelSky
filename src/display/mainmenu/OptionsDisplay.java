package display.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OptionsDisplay extends JPanel {

    public OptionsDisplay() {
        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "moveUp");
        this.getActionMap().put("moveUp", moveUp);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(300, 300, 300, 300);
    }
}
