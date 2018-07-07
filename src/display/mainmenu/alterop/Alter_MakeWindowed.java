package display.mainmenu.alterop;

import javax.swing.*;

public class Alter_MakeWindowed extends ViewAlterer {
    public Alter_MakeWindowed(String optionName, JFrame view) {
        super(optionName, view);
    }

    @Override
    public void changeView() {
        targetView.setVisible(false);
        targetView.dispose();
        targetView.setExtendedState(JFrame.NORMAL);
        targetView.setUndecorated(false);
        targetView.pack();
        targetView.setVisible(true);
    }
}
