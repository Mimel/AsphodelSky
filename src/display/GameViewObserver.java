package display;

import javax.swing.*;

public abstract class GameViewObserver extends JPanel {
    protected GameManager viewManager;

    protected void disableInputs() {
        setEnabled(false);
    }

    protected void enableInputs() {
        setEnabled(true);
    }

    protected void showInView() {
        setVisible(true);
    }

    protected void hideFromView() {
        setVisible(false);
    }

    public abstract void update();
}
