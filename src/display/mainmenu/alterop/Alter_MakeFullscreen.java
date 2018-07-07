package display.mainmenu.alterop;

import javax.swing.*;

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
