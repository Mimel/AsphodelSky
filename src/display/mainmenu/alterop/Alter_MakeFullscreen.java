package display.mainmenu.alterop;

import javax.swing.*;

/**
 * A view alterer that changes the view by removing all window decoration and extending the frame's bounds
 * to match those of the screen. Only works when the frame is "windowed".
 */
public class Alter_MakeFullscreen extends ViewAlterer {
    public Alter_MakeFullscreen(String optionName, JFrame view) {
        super(optionName, view);
    }

    @Override
    public void changeView() {
        targetView.setVisible(false);
        targetView.dispose();
        targetView.setExtendedState(JFrame.MAXIMIZED_BOTH);
        targetView.setUndecorated(true);
        targetView.pack();
        targetView.setVisible(true);
    }
}
