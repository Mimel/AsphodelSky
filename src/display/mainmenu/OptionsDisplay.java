package display.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class OptionsDisplay extends JPanel {

    private final OptionsLogic ol;

    public OptionsDisplay(OptionsLogic ol, WindowController wc) {
        this.ol = ol;
        Action goBack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wc.removeTopView();
            }
        };

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "goBack");
        this.getActionMap().put("goBack", goBack);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.fillRect(300, 300, 300, 300);
    }
}
