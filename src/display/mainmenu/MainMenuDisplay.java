package display.mainmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenuDisplay extends JPanel {
    private final MainMenuLogic mml;

    public MainMenuDisplay(MainMenuLogic mml, WindowController wc) {
        setPreferredSize(new Dimension(1200, 900));
        this.mml = mml;

        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionUp();
                repaint();
            }
        };

        Action moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionDown();
                repaint();
            }
        };

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wc.addViewToTop(mml.getSelectedView());
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
        for(int i = 0; i < mml.getNumberOfOptions(); i++) {
            if(i == mml.getSelectedOption()) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.BLACK);
            }

            g.drawString(mml.getOptionNameAtPosition(i), 100, 300 + (i * 30));
        }
    }
}
