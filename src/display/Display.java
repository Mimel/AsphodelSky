package display;

import java.awt.BorderLayout;

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
	 * CompositeGrid component, based on the left side of the window between the header and footer.
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
	void switchState(DisplayConfiguration newConfig) {
		switch(newConfig) {
			case DEFAULT:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.COMBATANT);
				fc.setCurrentMode(FooterMode.MESSAGES);
				break;

			case TILE_SELECT:
				gc.setCurrentMode(FocusMode.SELECTION);
				sc.setCurrentMode(SidebarMode.COMBATANT);
				fc.setCurrentMode(FooterMode.DESCRIPTION);
				break;

			case INVENTORY_SELECT:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.SELECTION);
				fc.setCurrentMode(FooterMode.DESCRIPTION);
				break;

			case DIALOGUE:
				gc.setCurrentMode(FocusMode.PLAYER_FOCUS);
				sc.setCurrentMode(SidebarMode.COMBATANT);
				fc.setCurrentMode(FooterMode.DIALOGUE);
				break;
		}

		currentConfig = newConfig;
	}

	/**
	 * Restricts the set of key bindings to just the arrow keys, exit, yes, and no. This is used in
	 * many selections, such as item select, tile select, and skill select.
	 */
	void restrictKeyBindings() {
		if(keyBindings == null) {
			keyBindings = getInputMap();
		}

		setInputMap(JComponent.WHEN_FOCUSED, getInputMap().getParent());
	}

	/**
	 * Expands the set of key bindings to every single set bind.
	 */
	void expandKeyBindings() {
		if(keyBindings != null) {
			setInputMap(JComponent.WHEN_FOCUSED, keyBindings);
		}
	}
}