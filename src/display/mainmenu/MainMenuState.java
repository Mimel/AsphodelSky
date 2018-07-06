package display.mainmenu;

import java.awt.*;

public class MainMenuState extends MenuState {

    private MainMenuLogic mml;

    public MainMenuState(MainMenuLogic mml) {
        this.mml = mml;
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
