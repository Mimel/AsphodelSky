package display.mainmenu;

import javax.swing.*;

/**
 * An option that, when selected in a JPanel, adds a new panel to the view stack.
 */
public interface ViewChanger {
    String getOptionName();

    JComponent getNewView();
}
