package display.mainmenu;

import javax.swing.*;

public abstract class ViewAlterer  {
    private final String optionName;
    private final JFrame targetView;

    ViewAlterer(String optionName, JFrame view) {
        this.optionName = optionName;
        this.targetView = view;
    }

    String getOptionName() {
        return optionName;
    }

    abstract void changeView();
}
