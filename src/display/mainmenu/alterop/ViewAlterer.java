package display.mainmenu.alterop;

import javax.swing.*;

/**
 * An option that chages the display settings of it's JFrame through the changeView method.
 */
public abstract class ViewAlterer  {
    private final String optionName;
    final JFrame targetView;

    ViewAlterer(String optionName, JFrame view) {
        this.optionName = optionName;
        this.targetView = view;
    }

    public String getOptionName() {
        return optionName;
    }

    public abstract void changeView();
}
