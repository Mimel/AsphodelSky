package display.mainmenu;

import display.WindowController;
import display.optionsmenu.OptionsDisplay;
import display.optionsmenu.OptionsLogic;

import javax.swing.*;

public class GoToOptionsMenu implements ViewChanger {
    private final String optionName;
    private final JFrame optionAlterationTarget;
    private final WindowController container;

    public GoToOptionsMenu(String optionName, JFrame alterationTarget, WindowController container) {
        this.optionName = optionName;
        this.optionAlterationTarget = alterationTarget;
        this.container = container;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public JComponent getNewView() {
        return new OptionsDisplay(new OptionsLogic(optionAlterationTarget), container);
    }
}
