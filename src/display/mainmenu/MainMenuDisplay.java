package display.mainmenu;

import javax.swing.*;
import java.awt.*;

public class MainMenuDisplay extends JPanel {
    MenuState currentDisplayState;

    public MainMenuDisplay() {
        currentDisplayState = new MainMenuState();
        currentDisplayState.setPreferredSize(new Dimension(1200, 900));
        add(currentDisplayState);
    }
}
