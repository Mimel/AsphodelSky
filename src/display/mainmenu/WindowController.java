package display.mainmenu;

import display.GameSession;

import javax.swing.*;
import java.util.Stack;

public class WindowController {
    JFrame view;
    Stack<JPanel> viewStack;

    public WindowController(JFrame view) {
        this.view = view;
        this.viewStack = new Stack<>();

        ViewChanger goToGame = new ViewChanger("Start", new GameSession(1200, 900));
        ViewChanger goToOptions = new ViewChanger("Options", new OptionsDisplay(new OptionsLogic(), this));

        MainMenuLogic mml = new MainMenuLogic(goToGame, goToOptions);
        MainMenuDisplay mmd = new MainMenuDisplay(mml, this);

        viewStack.add(mmd);
        this.view.add(mmd);
        this.view.pack();
        this.view.setVisible(true);
    }

    public void addViewToTop(JPanel newView) {
        view.remove(viewStack.peek());
        viewStack.push(newView);
        view.add(viewStack.peek());
        view.revalidate();
        view.repaint();
    }

    public void removeTopView() {
        view.remove(viewStack.pop());
        if(viewStack.isEmpty()) {
            System.exit(0);
        } else {
            view.add(viewStack.peek());
            view.revalidate();
            view.repaint();
        }
    }
}
