package display.game.focus;

import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.nvgRGB;

class NVGColorData {
    final NVGColor BLACK = NVGColor.create();
    final NVGColor RED = NVGColor.create();
    final NVGColor DARK_RED = NVGColor.create();

    NVGColorData() {
        nvgRGB((byte)0, (byte)0, (byte)0, BLACK);
        nvgRGB((byte)214, (byte)37, (byte)37, RED);
        nvgRGB((byte)149, (byte)25, (byte)25, DARK_RED);
    }
}
