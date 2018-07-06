package display.mainmenu;


import javax.swing.*;

class MainMenuLogic {
    private final ViewChanger[] optionItems;
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

    void moveSelectedOptionUp() {
        if(--selectedOption < 0) {
            selectedOption = optionItems.length - 1;
        }
    }

    void moveSelectedOptionDown() {
        if(++selectedOption >= optionItems.length) {
            selectedOption = 0;
        }
    }
}
