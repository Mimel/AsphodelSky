package display.game;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends JLayeredPane {
    private List<GameViewObserver> panels;
    private GameSessionViewState focusedPanel;

    public GameManager() {
        this.panels = new ArrayList<>();
    }

    void addObserver(GameViewObserver gvo) {
        panels.add(gvo);
    }

    boolean isFocus(GameSessionViewState viewState) {
        return focusedPanel.equals(viewState);
    }

    public void setFocusedPanel(GameSessionViewState newState) {
        this.focusedPanel = newState;
        updateAll();
    }

    private void updateAll() {
        for(GameViewObserver obs : panels) {
            obs.update();
        }
    }
}
