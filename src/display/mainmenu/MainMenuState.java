package display.mainmenu;

import javax.swing.*;
import java.awt.*;

public class MainMenuState extends MenuState {

    @Override
    protected void paintComponent(Graphics g) {
        for(int x = 0; x < 40; x++) {
            g.drawString("Hello world!", x * 10, x * 25);
        }

    }
}
