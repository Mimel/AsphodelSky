package display.mainmenu;

import javax.swing.*;

class ViewChanger {
    private final String optionName;
    private final JPanel newView;

    public ViewChanger(String optionName, JPanel view) {
        this.optionName = optionName;
        this.newView = view;
    }

    public String getOptionName() {
        return optionName;
    }

    public JPanel getNewView() {
        return newView;
    }
}
