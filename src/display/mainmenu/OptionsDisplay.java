package display.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * A JPanel that presents a set of display-altering options that work on the containing JFrame.
 */
class OptionsDisplay extends JPanel {

    /**
     * The model component for this view.
     */
    private final OptionsLogic ol;

    OptionsDisplay(OptionsLogic ol, WindowController wc) {
        this.ol = ol;

        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ol.moveSelectedOptionUp();
                repaint();
            }
        };

        Action moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ol.moveSelectedOptionDown();
                repaint();
            }
        };

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ol.adjustSelectedView();
                wc.refitView();
            }
        };

        Action goBack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wc.removeTopView();
            }
        };

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("UP"), "moveUp");
        this.getActionMap().put("moveUp", moveUp);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        this.getActionMap().put("moveDown", moveDown);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        this.getActionMap().put("confirm", confirm);

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "goBack");
        this.getActionMap().put("goBack", goBack);
    }

    @Override
    protected void paintComponent(Graphics g) {
        for(int i = 0; i < ol.getNumberOfOptions(); i++) {
            if(i == ol.getSelectedOption()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.BLACK);
            }

            g.drawString(ol.getOptionNameAtPosition(i), 100, 300 + (i * 30));
        }
    }
}
