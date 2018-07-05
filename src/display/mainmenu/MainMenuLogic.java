package display.mainmenu;

class MainMenuLogic {
    private final String[] optionItems;
    private int selectedOption;

    MainMenuLogic(String... options) {
        optionItems = options;
        selectedOption = 0;
    }

    int getNumberOfOptions() {
        return optionItems.length;
    }

    String getOptionNameAtPosition(int position) {
        return optionItems[position];
    }

    int getSelectedOption() {
        return selectedOption;
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
