package display.mainmenu;

import display.GameSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Stack;

public class MenuController {
    JFrame view;
    Stack<JPanel> viewStack;

    public MenuController(JFrame view) {
        this.view = view;
        this.viewStack = new Stack<>();

        ViewChanger goToGame = new ViewChanger("Start", new GameSession(1200, 900));
        ViewChanger goToOptions = new ViewChanger("Options", new OptionsDisplay());

        MainMenuLogic mml = new MainMenuLogic(goToGame, goToOptions);
        MainMenuDisplay mmd = new MainMenuDisplay(mml);

        viewStack.add(mmd);

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

        Action confirm = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.remove(viewStack.peek());
                view.add(mml.getSelectedView());
                viewStack.peek().requestFocusInWindow();
                view.revalidate();
                view.repaint();
                view.pack();
                view.setVisible(true);
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
