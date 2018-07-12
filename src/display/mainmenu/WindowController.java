package display.mainmenu;

import display.GameSession;
import display.mainmenu.alterop.Alter_AdjustResolution;
import display.mainmenu.alterop.Alter_MakeFullscreen;
import display.mainmenu.alterop.Alter_MakeWindowed;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Stack;

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
    private final Stack<JPanel> viewStack;

    public WindowController() {
        this.view = new JFrame("Asphodel Sky");
        view.setMinimumSize(new Dimension(400, 400));
        view.setMaximumSize(new Dimension(1920, 1080));
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.viewStack = new Stack<>();
        Alter_AdjustResolution aar1 = new Alter_AdjustResolution("Adjust to 800x600", 800, 600, view);
        Alter_AdjustResolution aar2 = new Alter_AdjustResolution("Adjust to 1200x900", 1200, 900, view);
        Alter_AdjustResolution aar3 = new Alter_AdjustResolution("Adjust to 1920x1080", 1920, 1080, view);
        Alter_MakeFullscreen amf = new Alter_MakeFullscreen("Fullscreen", view);
        Alter_MakeWindowed amw = new Alter_MakeWindowed("Windowed", view);

        ViewChanger goToGame = new ViewChanger("Start", new GameSession(1200, 900));
        ViewChanger goToOptions = new ViewChanger("Options", new OptionsDisplay(new OptionsLogic(aar1, aar2, aar3, amf, amw), this));

        MainMenuLogic mml = new MainMenuLogic(goToGame, goToOptions);
        MainMenuDisplay mmd = new MainMenuDisplay(mml, this);

        viewStack.add(mmd);
        this.view.add(mmd);
        this.view.pack();
        this.view.setVisible(true);
    }
    
    /**
     * Adds a new JPanel to the top of the view stack, as well as shown in the view instead of the previous JPanel.
     * The JFrame is revalidated to show this.
     */
    void addViewToTop(JPanel newView) {
        view.remove(viewStack.peek());
        viewStack.push(newView);
        view.add(viewStack.peek());
        refitView();
    }
    
    /**
     * Removes the top JPanel from the view stack. The JFrame will show the newest top JPanel.
     */
    void removeTopView() {
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
    void refitView() {
        view.revalidate();
        view.repaint();
    }
}
