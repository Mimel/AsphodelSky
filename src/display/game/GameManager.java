package display.game;

import display.music.AudioPlayer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends JLayeredPane {
    private List<GameViewObserver> panels;
    private GameSessionViewState focusedPanel;

    public GameManager(AudioPlayer ap) {
        this.panels = new ArrayList<>();
        this.add(new GameSession(1500, 600, ap, this), 0, 0);
        this.add(new InGamePauseMenu(1500, 600, this), 1, 0);
        this.setFocusedPanel(GameSessionViewState.GAME);
    }

    void addObserver(GameViewObserver gvo) {
        panels.add(gvo);
    }

    boolean isFocus(GameSessionViewState viewState) {
        return focusedPanel.equals(viewState);
    }

    void setFocusedPanel(GameSessionViewState newState) {
        this.focusedPanel = newState;
        updateAll();
    }

    private void updateAll() {
        for(GameViewObserver obs : panels) {
            obs.update();
        }
    }
}
