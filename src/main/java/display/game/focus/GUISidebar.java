package display.game.focus;

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

    private final float VERT_MARGIN = 50.0f;
    private final float HORZ_MARGIN = 50.0f;

    private float sidebarX;

    private float hiddenSidebarX;
    private float shownSidebarX;

    private float sidebarY;
    private final float SIDEBAR_WIDTH = 400.0f;
    private float sidebarHeight;

    private sidebarState visibility;

    private double previousTime;
    private final float TRANSITION_TIME = 0.2f;

    GUISidebar(Combatant focusedCombatant, int width, int height) {
        this.focusedCombatant = focusedCombatant;
        this.nvgContext = nvgCreate(NVG_STENCIL_STROKES | NVG_DEBUG);

        this.screenWidth = width;
        this.screenHeight = height;

        this.hiddenSidebarX = screenWidth;
        this.shownSidebarX = screenWidth - SIDEBAR_WIDTH - HORZ_MARGIN;

        this.sidebarX = hiddenSidebarX;
        this.sidebarY = VERT_MARGIN;
        this.sidebarHeight = screenHeight - (VERT_MARGIN * 2);

        this.visibility = sidebarState.OFF_SCREEN;
        this.previousTime = glfwGetTime();
    }

    void draw() {
        if(visibility == sidebarState.SLIDING_IN || visibility == sidebarState.SLIDING_OUT) {
            adjustSidebarLocation();
        }

        nvgBeginFrame(nvgContext, screenWidth, screenHeight, 1.0f);

        nvgBeginPath(nvgContext);
        nvgRoundedRect(nvgContext, sidebarX, sidebarY, SIDEBAR_WIDTH, sidebarHeight, 5);
        nvgFill(nvgContext);

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
