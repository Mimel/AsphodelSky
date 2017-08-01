package display;

import java.awt.BorderLayout;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;

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
	 * The current display configuration.
	 */
	private DisplayConfiguration currentConfig;

	/**
	 * A queue of required requests that must be fulfilled before the configuration resets to default.
	 */
	private Deque<DisplayPrompt> promptQueue;

	/**
	 * A stack of requests that have been fulfilled.
	 */
	private Stack<DisplayPrompt> usedPromptStack;

	/**
	 * The set of key bindings to use.
	 */
	private InputMap keyBindings;

	public Display(int winWidth, int winHeight) {
		this.setLayout(new BorderLayout());
		
		//Magic numbers soon to be replaced
		this.gc = new GUIFocus(0, 0, 624, 624, 48);
		this.sc = new GUISidebar(624, 50, winWidth - 624, 624);
		this.fc = new GUIFooter(0, 624, winWidth, winHeight - 624);

		this.add(gc, BorderLayout.WEST);
		this.add(sc, BorderLayout.EAST);
		this.add(fc, BorderLayout.SOUTH);

		this.currentConfig = DisplayConfiguration.DEFAULT;

		this.promptQueue = new LinkedList<>();
		this.usedPromptStack = new Stack<>();
	}

	public GUIFocus getFocus() { return gc; }
	public GUISidebar getSidebar() { return sc;}
	public GUIFooter getFooter() { return fc; }
	DisplayConfiguration getConfig() {
		return currentConfig;
	}

	/**
	 * Switches the configuration of the display. The configuration determines the information that is
	 * displayed on the screen. When switching the state, the previous state is overridden.
	 * @param newConfig The new configuration to use.
	 */
	private void switchState(DisplayConfiguration newConfig) {
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

	boolean isPromptQueueEmpty() {
		return promptQueue.isEmpty();
	}

	boolean isUsedStackEmpty() {
		return usedPromptStack.isEmpty();
	}

	/**
	 * Enqueues a prompt onto this display's prompt queue. If a prompt is added when there are no prompts on the
	 * queue, the key binds are restricted, allowing only arrow key movements, yes, no, and game exit commands.
	 * @param dp The display prompt to add to the queue.
	 */
	void enqueuePrompt(DisplayPrompt dp) {
		if(promptQueue.isEmpty()) {
			restrictKeyBindings();
			switch(dp) {
				case ITEM_PROMPT:
					switchState(DisplayConfiguration.INVENTORY_SELECT);
					break;
				case TILE_PROMPT:
					switchState(DisplayConfiguration.TILE_SELECT);
					break;
			}
		}

		promptQueue.addLast(dp);
	}

	DisplayPrompt peekPrompt() {
		return promptQueue.peekFirst();
	}

	/**
	 * Adds the prompt on the top of the used prompt stack back to the head of the queue.
	 * @return The prompt that was added to the head of the queue.
	 */
	DisplayPrompt requeuePrompt() {
		if(!usedPromptStack.isEmpty()) {
			promptQueue.addFirst(usedPromptStack.pop());
			switch(promptQueue.peekFirst()) {
				case ITEM_PROMPT:
					switchState(DisplayConfiguration.INVENTORY_SELECT);
					break;
				case TILE_PROMPT:
					switchState(DisplayConfiguration.TILE_SELECT);
					break;
			}
		}

		return promptQueue.peekFirst();
	}

	/**
	 * Dequeues a prompt from this display's prompt queue. If there are no prompts left after removal, then
	 * the key binds are unrestricted, allowing for complete control of the game.
	 * @return The prompt that was removed.
	 */
	DisplayPrompt dequeuePrompt() {
		DisplayPrompt dp = promptQueue.pollFirst();
		usedPromptStack.push(dp);

		if(promptQueue.isEmpty()) {
			switchState(DisplayConfiguration.DEFAULT);
			usedPromptStack.clear();
			expandKeyBindings();
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

	/**
	 * Clears the prompt queue, and unrestricts keybinds if there are any removed prompts.
	 */
	void clearPromptQueue() {
		if(!isPromptQueueEmpty()) {
			expandKeyBindings();
			promptQueue.clear();
			usedPromptStack.clear();
			switchState(DisplayConfiguration.DEFAULT);
		}
	}

	/**
	 * Restricts the set of key bindings to just the arrow keys, exit, yes, and no. This is used in
	 * many selections, such as item select, tile select, and skill select.
	 */
	private void restrictKeyBindings() {
		if(keyBindings == null) {
			keyBindings = getInputMap();
		}

		setInputMap(JComponent.WHEN_FOCUSED, getInputMap().getParent());
	}

	/**
	 * Expands the set of key bindings to every single set bind.
	 */
	private void expandKeyBindings() {
		if(keyBindings != null) {
			setInputMap(JComponent.WHEN_FOCUSED, keyBindings);
		}
	}
}