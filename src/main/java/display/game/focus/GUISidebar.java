package display.game.focus;

import display.image.ImageAssets;
import entity.Combatant;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;

public class GUISidebar {

    private enum sidebarState {
        OFF_SCREEN,
        ON_SCREEN,
        SLIDING_IN,
        SLIDING_OUT
    }

    private Combatant focusedCombatant;
    private long nvgContext;

    private int screenWidth;
    private int screenHeight;

    private float sidebarX;

    private float hiddenSidebarX;
    private float shownSidebarX;

    private float sidebarY;
    private final float SIDEBAR_WIDTH = 400.0f;
    private float sidebarHeight;

    private sidebarState visibility;

    private double previousTime;
    private final float TRANSITION_TIME = 0.2f;

    private final FontData fonts;
    private final NVGColorData colors;

    private final SidebarInventory drawnInventory;
    private final SidebarSkills drawnSkills;

    GUISidebar(Combatant focusedCombatant, int width, int height, ImageAssets ia) {
        this.focusedCombatant = focusedCombatant;
        this.nvgContext = nvgCreate(NVG_STENCIL_STROKES | NVG_DEBUG);

        this.screenWidth = width;
        this.screenHeight = height;

        this.hiddenSidebarX = screenWidth;
        float HORZ_MARGIN = 50.0f;
        this.shownSidebarX = screenWidth - SIDEBAR_WIDTH - HORZ_MARGIN;

        this.sidebarX = hiddenSidebarX;
        float VERT_MARGIN = 50.0f;
        this.sidebarY = VERT_MARGIN;
        this.sidebarHeight = screenHeight - (VERT_MARGIN * 2);

        this.visibility = sidebarState.OFF_SCREEN;
        this.previousTime = glfwGetTime();

        this.fonts = new FontData(nvgContext);
        this.colors = new NVGColorData();

        this.drawnInventory = new SidebarInventory(focusedCombatant, sidebarX, sidebarY + 300.0f, ia);
        this.drawnSkills = new SidebarSkills(focusedCombatant, sidebarX, sidebarY + 500.0f);
    }

    void draw() {
        if(visibility == sidebarState.SLIDING_IN || visibility == sidebarState.SLIDING_OUT) {
            adjustSidebarLocation();
        }

        nvgBeginFrame(nvgContext, screenWidth, screenHeight, 1.0f);

        nvgBeginPath(nvgContext);
        nvgRoundedRect(nvgContext, sidebarX, sidebarY, SIDEBAR_WIDTH, sidebarHeight, 5);
        nvgFill(nvgContext);

        nvgBeginPath(nvgContext);
        nvgFillColor(nvgContext, colors.RED);
        nvgRect(nvgContext, sidebarX + 20.0f, sidebarY + 40.0f, 300.0f, 50.0f);
        nvgFill(nvgContext);

        nvgFontSize(nvgContext, 85.5f);
        nvgFontFace(nvgContext, fonts.MULI_BOLD);
        nvgFillColor(nvgContext, colors.DARK_RED);
        nvgTextAlign(nvgContext, NVG_ALIGN_BASELINE);
        nvgText(nvgContext, sidebarX + 40.0f, sidebarY + 90.0f, focusedCombatant.getHealth() + "");
        float nextChar = nvgText(nvgContext, sidebarX + 40.0f, sidebarY + 90.0f, focusedCombatant.getHealth() + "");

        nvgFontSize(nvgContext, 30.0f);
        nvgFontFace(nvgContext, fonts.MULI);
        nvgTextAlign(nvgContext, NVG_ALIGN_BASELINE);
        nvgText(nvgContext, nextChar, sidebarY + 90.0f - 15.5f, "/" + focusedCombatant.getMaxHealth());

        drawnInventory.draw(nvgContext);
        drawnSkills.draw(nvgContext);

        nvgEndFrame(nvgContext);
    }

    void show() {
        if(visibility == sidebarState.OFF_SCREEN || visibility == sidebarState.SLIDING_OUT) {
            visibility = sidebarState.SLIDING_IN;
            previousTime = glfwGetTime();
        }
    }

    void hide() {
        if(visibility == sidebarState.ON_SCREEN || visibility == sidebarState.SLIDING_IN) {
            visibility = sidebarState.SLIDING_OUT;
            previousTime = glfwGetTime();
        }
    }

    private void adjustSidebarLocation() {
        double currentTime = glfwGetTime();
        double timeDifference = currentTime - previousTime;
        if(visibility == sidebarState.SLIDING_IN) {
            if(timeDifference >= TRANSITION_TIME) {
                sidebarX = shownSidebarX;
                visibility = sidebarState.ON_SCREEN;
            } else {
                sidebarX = hiddenSidebarX - ((float)(timeDifference / TRANSITION_TIME) * (hiddenSidebarX - shownSidebarX));
            }
        } else if(visibility == sidebarState.SLIDING_OUT) {
            if(timeDifference >= TRANSITION_TIME) {
                sidebarX = hiddenSidebarX;
                visibility = sidebarState.OFF_SCREEN;
            } else {
                sidebarX = (float)(timeDifference / TRANSITION_TIME) * (hiddenSidebarX - shownSidebarX) + shownSidebarX;
            }
        }
    }
}
