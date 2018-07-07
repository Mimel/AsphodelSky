package display.mainmenu;

public class OptionsLogic {
    private final ViewAlterer[] adjustmentOptions;
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

    void moveSelectedOptionUp() {
        if(--selectedOption < 0) {
            selectedOption = adjustmentOptions.length - 1;
        }
    }

    void moveSelectedOptionDown() {
        if(++selectedOption >= adjustmentOptions.length) {
            selectedOption = 0;
        }
    }
}
