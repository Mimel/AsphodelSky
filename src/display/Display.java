package display;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;

/**
 * The whole of the GUI of Asphodel Sky. Contains one instance of each subclass of the DisplayComponent class,
 * and coordinates, updates, and sends information about each component.
 * @author Matt Imel
 *
 */
public class Display extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Grid component, based on the left side of the window between the header and footer.
	 */
	private GUIFocus gc;
	
	/**
	 * Sidebar component, based on the right side of the window between the header and footer.
	 */
	private GUISidebar sc;
	
	/**
	 * Footer component, based on the bottom of the window.
	 */
	private GUIFooter fc;
	
	/**
	 * Header component, based on the top of the window.
	 */
	private GUIHeader hc;

	/**
	 * The current display configuration.
	 */
	private DisplayConfiguration currentConfig;

	/**
	 * A queue of required requests that must be fulfilled before the configuration resets to default.
	 */
	private Queue<DisplayPrompt> promptQueue;

	/**
	 * The unused input map. Contains either the restricted or unrestricted keybinds, opposite of the current set.
	 */
	private InputMap unusedInputMap;

	/**
	 * The unused action map. Contains either the restricted or unrestricted keybinds, opposite of the current set.
	 */
	private ActionMap unusedActionMap;

	public Display(int winWidth, int winHeight) {
		this.setLayout(new BorderLayout());
		
		//Magic numbers soon to be replaced
		this.hc = new GUIHeader(0, 0, winWidth, 50);
		this.gc = new GUIFocus(0, 50, 624, 624, 48);
		this.sc = new GUISidebar(624, 50, winWidth - 624, 624);
		this.fc = new GUIFooter(0, 50 + 624, winWidth, winHeight - 624 - 50);
		
		this.add(hc, BorderLayout.NORTH);
		this.add(gc, BorderLayout.WEST);
		this.add(sc, BorderLayout.EAST);
		this.add(fc, BorderLayout.SOUTH);

		this.currentConfig = DisplayConfiguration.DEFAULT;

		this.promptQueue = new LinkedList<DisplayPrompt>();
	}
	
	public GUIHeader getHeader() { return hc; }
	public GUIFocus getFocus() { return gc; }
	public GUISidebar getSidebar() { return sc;}
	public GUIFooter getFooter() { return fc; }
	public DisplayConfiguration getConfig() {
		return currentConfig;
	}

	public void switchState(DisplayConfiguration newConfig) {
		switch(newConfig) {
			case DEFAULT:
				gc.setCurrentMode("player");
				sc.setCurrentMode("free");
				fc.setCurrentMode("free");
				break;

			case TILE_SELECT:
				gc.setCurrentMode("crosshair");
				sc.setCurrentMode("free");
				fc.setCurrentMode("descript");
				break;

			case INVENTORY_SELECT:
				gc.setCurrentMode("player");
				sc.setCurrentMode("inventory");
				fc.setCurrentMode("descript");
				break;
		}

		currentConfig = newConfig;
	}

	public void initializeRestrictedCharacterSet(InputMap im, ActionMap am) {
		unusedInputMap = im;
		unusedActionMap = am;
	}

	private void toggleCharacterSet() {
		InputMap swapIM = getInputMap();
		ActionMap swapAM = getActionMap();

		this.setInputMap(JComponent.WHEN_FOCUSED, unusedInputMap);
		this.setActionMap(unusedActionMap);

		this.unusedInputMap = swapIM;
		this.unusedActionMap = swapAM;
	}

	public boolean isPromptQueueEmpty() {
		return promptQueue.isEmpty();
	}

	public void enqueuePrompt(DisplayPrompt dp) {
		if(promptQueue.isEmpty()) {
			toggleCharacterSet();
			switch(dp) {
				case ITEM_PROMPT:
					switchState(DisplayConfiguration.INVENTORY_SELECT);
					break;
				case TILE_PROMPT:
					switchState(DisplayConfiguration.TILE_SELECT);
					break;
			}
		}

		promptQueue.add(dp);
	}

	public DisplayPrompt peekPrompt() {
		return promptQueue.peek();
	}

	public DisplayPrompt dequeuePrompt() {
		DisplayPrompt dp = promptQueue.remove();

		if(promptQueue.isEmpty()) {
			toggleCharacterSet();
			switchState(DisplayConfiguration.DEFAULT);
		} else {
			switch(promptQueue.peek()) {
				case ITEM_PROMPT:
					switchState(DisplayConfiguration.INVENTORY_SELECT);
					break;
				case TILE_PROMPT:
					switchState(DisplayConfiguration.TILE_SELECT);
					break;
			}
		}

		return dp;
	}

	public void clearPromptQueue() {
		promptQueue.clear();
	}
}