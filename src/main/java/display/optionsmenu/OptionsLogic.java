package display.optionsmenu;

import javax.swing.*;

/**
 * The model for the framework for the Options Menu.
 */
public class OptionsLogic {
  
    /**
     * A set of view-altering options that can be selected.
     */
    private final ViewAlterer[] adjustmentOptions;
    
    /**
     * The currently selected option. Must be an integer between
     * zero and one less than the size of adjustmentOptions.
     */
    private int selectedOption;

    public OptionsLogic(JFrame view) {
        Alter_AdjustResolution aar1 = new Alter_AdjustResolution("Adjust to 800x600", 800, 600, view);
        Alter_AdjustResolution aar2 = new Alter_AdjustResolution("Adjust to 1200x900", 1200, 900, view);
        Alter_AdjustResolution aar3 = new Alter_AdjustResolution("Adjust to 1920x1080", 1920, 1080, view);
        Alter_MakeFullscreen amf = new Alter_MakeFullscreen("Fullscreen", view);
        Alter_MakeWindowed amw = new Alter_MakeWindowed("Windowed", view);

        this.adjustmentOptions = new ViewAlterer[]{aar1, aar2, aar3, amf, amw};
        selectedOption = 0;
    }

    int getNumberOfOptions() {
        return adjustmentOptions.length;
    }

    String getOptionNameAtPosition(int position) {
        return adjustmentOptions[position].getOptionName();
    }

    int getSelectedOption() {
        return selectedOption;
    }

    void adjustSelectedView() {
        adjustmentOptions[selectedOption].changeView();
    }

    /**
     * Moves the selected option "up" by reducing its value by one.
     * If the selected option already equals zero, the selected option
     * loops around and becomes its maximum.
     */
    void moveSelectedOptionUp() {
        if(--selectedOption < 0) {
            selectedOption = adjustmentOptions.length - 1;
        }
    }

    /**
     * Moves the selected option "down" by increasing its value by one.
     * If the selected option already equals one less than the number of
     * adjustment options, the selected option loops around and becomes zero.
     */
    void moveSelectedOptionDown() {
        if(++selectedOption >= adjustmentOptions.length) {
            selectedOption = 0;
        }
    }
}
