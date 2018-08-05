package display;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;

public class FontData {
    int muli;

    public FontData(long nvgContext) {
        nvgCreateFont(nvgContext, "muli", "fonts/Muli/Muli-Regular.ttf");
    }
}
