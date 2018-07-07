package display.mainmenu.alterop;

import javax.swing.*;
import java.awt.*;

public class Alter_AdjustResolution extends ViewAlterer {
    private final Dimension bounds;

    public Alter_AdjustResolution(String optionName, int width, int height, JFrame view) {
        super(optionName, view);
        this.bounds = new Dimension(width, height);
    }

    @Override
    public void changeView() {
        targetView.setPreferredSize(bounds);
        targetView.pack();
    }
}
