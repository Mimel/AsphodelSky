package display.mainmenu;

import javax.swing.*;

/**
 * The model for the Main Menu framework.
 */
class MainMenuLogic {
  
    /**
     * The set of JPanels that can potentially be inserted into the view stack.
     * Must not be empty.
     */
    private final ViewChanger[] optionItems;
    
    /**
     * The currently selected JPanel. Must be a number between zero and one less than the
     * number of elements in optionItems.
     */
    private int selectedOption;

    MainMenuLogic(ViewChanger... options) {
        optionItems = options;
        selectedOption = 0;
    }

    int getNumberOfOptions() {
        return optionItems.length;
    }

    String getOptionNameAtPosition(int position) {
        return optionItems[position].getOptionName();
    }

    int getSelectedOption() {
        return selectedOption;
    }

    JPanel getSelectedView() {
        return optionItems[selectedOption].getNewView();
    }
    
    /**
     * Moves the selected option "up" by reducing its value by one.
     * If the selected option already equals zero, the selected option
     * loops around and becomes its maximum.
     */
    void moveSelectedOptionUp() {
        if(--selectedOption < 0) {
            selectedOption = optionItems.length - 1;
        }
    }
    
    /**
     * Moves the selected option "down" by increasing its value by one.
     * If the selected option already equals one less than the number of
     * item options, the selected option loops around and becomes zero.
     */
    void moveSelectedOptionDown() {
        if(++selectedOption >= optionItems.length) {
            selectedOption = 0;
        }
    }

    boolean moveSelectedOptionTo(int option) {
        if(selectedOption != option && option >= 0 && option < optionItems.length) {
            selectedOption = option;
            return true;
        }

        return false;
    }
}
