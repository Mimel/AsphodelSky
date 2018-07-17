package display;

import display.game.GameManager;
import display.game.GameSession;
import display.game.GameSessionViewState;
import display.game.InGamePauseMenu;
import display.mainmenu.MainMenuDisplay;
import display.mainmenu.MainMenuLogic;
import display.mainmenu.ViewChanger;
import display.mainmenu.alterop.Alter_AdjustResolution;
import display.mainmenu.alterop.Alter_MakeFullscreen;
import display.mainmenu.alterop.Alter_MakeWindowed;
import display.music.AudioPlayer;
import display.optionsmenu.OptionsDisplay;
import display.optionsmenu.OptionsLogic;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A controller that manages the JFrame and the JPanels that may be implanted in it.
 */
public class WindowController {
    /**
     * The main display of the program.
     */
    private final JFrame view;
    
    /**
     * The stack that denotes the order of the JPanels implanted into the JFrame.
     * The topmost JPanel is the one currently being shown.
     */
    private final Stack<JComponent> viewStack;

    public WindowController() {
        this.view = new JFrame("Asphodel Sky");
        view.setMinimumSize(new Dimension(400, 400));
        view.setMaximumSize(new Dimension(1920, 1080));
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        AudioPlayer ap = new AudioPlayer("audio/music", "audio/sfx");
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(ap);

        this.viewStack = new Stack<>();
        Alter_AdjustResolution aar1 = new Alter_AdjustResolution("Adjust to 800x600", 800, 600, view);
        Alter_AdjustResolution aar2 = new Alter_AdjustResolution("Adjust to 1200x900", 1200, 900, view);
        Alter_AdjustResolution aar3 = new Alter_AdjustResolution("Adjust to 1920x1080", 1920, 1080, view);
        Alter_MakeFullscreen amf = new Alter_MakeFullscreen("Fullscreen", view);
        Alter_MakeWindowed amw = new Alter_MakeWindowed("Windowed", view);

        GameManager jlp = new GameManager();
        jlp.add(new GameSession(1200, 900, ap, jlp), 0, 0);
        jlp.add(new InGamePauseMenu(1200, 900, jlp), 1, 0);
        jlp.setFocusedPanel(GameSessionViewState.GAME);
        ViewChanger goToGame = new ViewChanger("Start", jlp);
        ViewChanger goToOptions = new ViewChanger("Options", new OptionsDisplay(new OptionsLogic(aar1, aar2, aar3, amf, amw), this));

        MainMenuLogic mml = new MainMenuLogic(goToGame, goToOptions);
        MainMenuDisplay mmd = new MainMenuDisplay(mml, this, ap);

        viewStack.add(mmd);
        this.view.add(mmd);
        this.view.pack();
        this.view.setVisible(true);
    }
    
    /**
     * Adds a new JPanel to the top of the view stack, as well as shown in the view instead of the previous JPanel.
     * The JFrame is revalidated to show this.
     */
    public void addViewToTop(JComponent newView) {
        view.remove(viewStack.peek());
        viewStack.push(newView);
        view.add(viewStack.peek());
        refitView();
    }
    
    /**
     * Removes the top JPanel from the view stack. The JFrame will show the newest top JPanel.
     */
    public void removeTopView() {
        view.remove(viewStack.pop());
        if(viewStack.isEmpty()) {
            System.exit(0);
        } else {
            view.add(viewStack.peek());
            refitView();
        }
    }

    /**
     * Performs necessary JFrame revalidation after removing/adding JPanels.
     */
    public void refitView() {
        view.revalidate();
        view.repaint();
    }
}
