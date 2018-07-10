package display.mainmenu;

import display.mainmenu.alterop.ViewAlterer;

/**
 * The model for the framework for the Options Menu.
 */
class OptionsLogic {
  
    /**
     * A set of view-altering options that can be selected.
     */
    private final ViewAlterer[] adjustmentOptions;
    
    /**
     * The currently selected option. Must be an integer between
     * zero and one less than the size of adjustmentOptions.
     */
    private int selectedOption;

    OptionsLogic(ViewAlterer... adjustmentOptions) {
        this.adjustmentOptions = adjustmentOptions;
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
