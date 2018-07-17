package display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class InGamePauseMenu extends GameViewObserver {

    public InGamePauseMenu(int winWidth, int winHeight, GameManager gm) {
        this.setBounds(0, 0, winWidth, winHeight);
        this.setOpaque(false);
        this.setVisible(true);
        this.viewManager = gm;
        this.viewManager.addObserver(this);


        Action goBackToGame = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewManager.setFocusedPanel(GameSessionViewState.GAME);
            }
        };

        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "goBackToGame");
        this.getActionMap().put("goBackToGame", goBackToGame);
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("Please draw.");
        g.setColor(Color.MAGENTA);
        g.fillRect(400, 400, 400, 400);
    }

    @Override
    public void update() {
        if(viewManager.isFocus(GameSessionViewState.PAUSE_MENU_MAIN)) {
            enableInputs();
            showInView();
        } else {
            disableInputs();
            hideFromView();
        }
    }
}
