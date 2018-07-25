package display.game;

import display.game.focus.GUIFocus;
import display.game.footer.GUIFooter;
import display.game.sidebar.GUISidebar;

import java.awt.*;

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class GameView extends GameViewObserver {

	private final GUIFocus focus;

	private final GUISidebar sidebar;

	private final GUIFooter footer;

	private DisplayConfiguration currentConfig;

	GameView(int winWidth, int winHeight, GUIFocus focus, GUISidebar sidebar, GUIFooter footer, GameManager gm) {
		this.setLayout(new BorderLayout());

		this.setBounds(0, 0, winWidth, winHeight);
		this.setDoubleBuffered(true);

		this.currentConfig = DisplayConfiguration.DEFAULT;

		this.focus = focus;
		this.sidebar = sidebar;
		this.footer = footer;

		this.viewManager = gm;
		this.viewManager.addObserver(this);

		repaint();
	}

	DisplayConfiguration getConfig() {
		return currentConfig;
	}

	/**
	 * Switches the configuration of the display. The configuration determines the information that is
	 * displayed on the screen. When switching the state, the previous state is overridden.
	 * @param newConfig The new configuration to use.
	 */
	void switchState(DisplayConfiguration newConfig) {
		switch(newConfig) {
			case DIALOGUE:
				sidebar.switchToNoSelect();
				footer.switchToDialogue();
				break;
			case TILE_SELECT:
				sidebar.switchToNoSelect();
				footer.switchToSrcDesc();
				break;
			case SKILL_SELECT:
				sidebar.switchToSkillSelect();
				footer.switchToSrcDesc();
				break;
			case INVENTORY_SELECT:
				sidebar.switchToItemSelect();
				footer.switchToSrcDesc();
				break;
			case DEFAULT:
				sidebar.switchToNoSelect();
				footer.switchToMessages();
				break;
		}
		currentConfig = newConfig;
	}

	@Override
	protected void paintComponent(Graphics g) {
		//long timeStart = System.currentTimeMillis();

		focus.paint(g);
		sidebar.paint(g);
		footer.paint(g);

		//long timeEnd = System.currentTimeMillis();
		//System.out.println("Paint time: " + (timeEnd - timeStart) + "ms.");
	}

	void pause() {
		viewManager.setFocusedPanel(GameSessionViewState.PAUSE_MENU_MAIN);
	}

	@Override
	public void update() {
		if(viewManager.isFocus(GameSessionViewState.GAME)) {
			enableInputs();
		} else {
			disableInputs();
		}
	}
}