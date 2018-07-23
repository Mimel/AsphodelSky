package display.game;

import comm.MessageManager;
import comm.SourceDescriptionTriplet;
import display.game.focus.GUIFocus;
import display.game.footer.FooterDialogue;
import display.game.footer.FooterMessageFeed;
import display.game.footer.FooterShortDescriptor;
import display.game.footer.GUIFooter;
import display.game.sidebar.GUISidebar;
import display.image.ImageAssets;
import event.EventQueue;
import event.InstructionSet;
import event.ResponseTable;
import grid.CompositeGrid;
import grid.creation.GridLoader;
import item.ItemPromptLibrary;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameManager extends JLayeredPane {
    private List<GameViewObserver> panels;
    private GameSessionViewState focusedPanel;

    public GameManager(GridLoader gridLoader) {
        this.panels = new ArrayList<>();

        CompositeGrid grid = gridLoader.loadGrid();
        EventQueue eq = new EventQueue(new InstructionSet(new ResponseTable("map/responsemap.dat")));
        ImageAssets ia = new ImageAssets();
        MessageManager mm = new MessageManager();
        SourceDescriptionTriplet sdt = new SourceDescriptionTriplet();

        GameView game = new GameView(1500, 1000,
                new GUIFocus(0, 0, 1500, 1000, grid, ia),
                new GUISidebar(0, 0, 500, 800, grid.getPlayer(), ia),
                new GUIFooter(0, 1200, 1500, 300, new FooterMessageFeed(mm), new FooterShortDescriptor(sdt), new FooterDialogue()),
                this);

        DisplayKeyBindings.initKeyBinds(game, grid, mm, sdt, eq, new ItemPromptLibrary("map/item_promptmap.dat"));

        this.add(game, 0, 0);
        this.add(new InGamePauseMenu(1500, 1000, this), 1, 0);
        this.setFocusedPanel(GameSessionViewState.GAME);

    }

    void addObserver(GameViewObserver gvo) {
        panels.add(gvo);
    }

    boolean isFocus(GameSessionViewState viewState) {
        return focusedPanel.equals(viewState);
    }

    void setFocusedPanel(GameSessionViewState newState) {
        this.focusedPanel = newState;
        updateAll();
    }

    private void updateAll() {
        for(GameViewObserver obs : panels) {
            obs.update();
        }
    }
}
