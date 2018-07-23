package display.mainmenu;

import display.game.GameManager;
import grid.creation.GridLoaderRectangles;

import javax.swing.*;

public class GoToNewGame implements ViewChanger {
    private final String optionName;

    public GoToNewGame(String optionName) {
        this.optionName = optionName;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public JComponent getNewView() {
        return new GameManager(new GridLoaderRectangles());
    }
}
