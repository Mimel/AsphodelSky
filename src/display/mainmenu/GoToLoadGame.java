package display.mainmenu;

import display.game.GameManager;
import grid.creation.GridLoaderFromFile;
import item.ItemLibrary;
import skill.SkillLibrary;

import javax.swing.*;

public class GoToLoadGame implements ViewChanger {
    private final String optionName;
    private final String fileName;

    public GoToLoadGame(String optionName, String fileToLoadGameFrom) {
        this.optionName = optionName;
        this.fileName = fileToLoadGameFrom;
    }

    @Override
    public String getOptionName() {
        return optionName;
    }

    @Override
    public JComponent getNewView() {
        return new GameManager(new GridLoaderFromFile(fileName, new ItemLibrary("map/item_effectmap.dat"), new SkillLibrary("map/skill_effectmap.dat")));
    }
}
