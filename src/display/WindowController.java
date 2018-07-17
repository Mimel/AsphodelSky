package display;

import display.game.GameManager;
import display.mainmenu.MainMenuDisplay;
import display.mainmenu.MainMenuLogic;
import display.mainmenu.ViewChanger;
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
        view = new JFrame();
        initializeFrame(new Dimension(400, 400), new Dimension(1920, 1080));

        AudioPlayer ap = new AudioPlayer("audio/music", "audio/sfx");
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(ap);

        this.viewStack = new Stack<>();
        ViewChanger goToGame = new ViewChanger("Start", new GameManager(ap));
        ViewChanger goToOptions = new ViewChanger("Options", new OptionsDisplay(new OptionsLogic(view), this));
        MainMenuLogic mml = new MainMenuLogic(goToGame, goToOptions);
        MainMenuDisplay mmd = new MainMenuDisplay(mml, this, ap);

        addViewToTop(mmd);

        this.view.pack();
        this.view.setVisible(true);
    }
    
    /**
     * Adds a new JPanel to the top of the view stack, as well as shown in the view instead of the previous JPanel.
     * The JFrame is revalidated to show this.
     */
    public void addViewToTop(JComponent newView) {
        if(!viewStack.isEmpty()) {
            view.remove(viewStack.peek());
        }
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

    private void initializeFrame(Dimension minSize, Dimension maxSize) {
        view.setTitle("Asphodel Sky");
        view.setMinimumSize(minSize);
        view.setMaximumSize(maxSize);
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
