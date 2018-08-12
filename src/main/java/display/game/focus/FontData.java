package display.game.focus;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;

class FontData {
    final String MULI = "muli";
    final String MULI_BOLD = "muli_bold";

    FontData(long nvgContext) {
        nvgCreateFont(nvgContext, MULI, "fonts/Muli/Muli-Regular.ttf");
        nvgCreateFont(nvgContext, MULI_BOLD, "fonts/Muli/Muli-Bold.ttf");
    }
}
