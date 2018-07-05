package display.mainmenu;

import display.GameSession;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MenuController {
    JFrame view;

    public MenuController(JFrame view) {
        this.view = view;
        MainMenuLogic mml = new MainMenuLogic("Start", "Options", "Exit to Desktop");
        MainMenuDisplay mmd = new MainMenuDisplay(mml);

        Action moveUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionUp();
                mmd.repaint();
            }
        };

        Action moveDown = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mml.moveSelectedOptionDown();
                mmd.repaint();
            }
        };

        //TODO TEST
        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.removeAll();
                view.add(new GameSession(1200, 900));
                view.pack();
            }
        };

        Action goBack = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        mmd.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "moveUp");
        mmd.getActionMap().put("moveUp", moveUp);

        mmd.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        mmd.getActionMap().put("moveDown", moveDown);

        mmd.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "confirm");
        mmd.getActionMap().put("confirm", confirm);

        mmd.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ESCAPE"), "goBack");
        mmd.getActionMap().put("goBack", goBack);

        this.view.add(mmd);
        this.view.pack();
        this.view.setVisible(true);
    }
}
