package display.mainmenu;

import javax.swing.*;

/**
 * An option that, when selected in a JPanel, adds a new panel to the view stack.
 */
class ViewChanger {
  
    /**
     * The display name of the option.
     */
    private final String optionName;
    
    /**
     * The view associated with the option. Most likely, when the option is selected,
     * this view will be put on top of the view stack.
     */
    private final JComponent newView;

    ViewChanger(String optionName, JComponent view) {
        this.optionName = optionName;
        this.newView = view;
    }

    String getOptionName() {
        return optionName;
    }

    JComponent getNewView() {
        return newView;
    }
}
