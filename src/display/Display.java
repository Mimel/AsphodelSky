package display;

import java.awt.BorderLayout;

import javax.swing.JPanel;

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
	}
	
	public GUIHeader getHeader() { return hc; }
	public GUIFocus getFocus() { return gc; }
	public GUISidebar getSidebar() { return sc;}
	public GUIFooter getFooter() { return fc; }
	
	/**
	 * Retrieves the current state of the grid.
	 * @return The grid state.
	 */
	public String getGridState() {
		return gc.selectedMode;
	}
	
	/**
	 * Switches the grid state with another. If the entered state is invalid, nothing happens.
	 * @param newState The new grid state to switch to.
	 */
	public void switchGridState(String newState) {
		gc.setCurrentMode(newState);
	}
	
	/**
	 * Retrieves the current state of the sidebar.
	 * @return The sidebar state.
	 */
	public String getSidebarState() {
		return sc.selectedMode;
	}
	
	/**
	 * Switches the sidebar state with another. If the entered state is invalid, nothing happens.
	 * @param newState The new sidebar state to switch to.
	 */
	public void switchSidebarState(String newState) {
		sc.setCurrentMode(newState);
	}
	
	/**
	 * Retrieves the current state of the footer.
	 * @return The footer state.
	 */
	public String getFooterState() {
		return fc.selectedMode;
	}
	
	/**
	 * Switches the footer state with another. If the entered state is invalid, nothing happens.
	 * @param newState The new footer state to switch to.
	 */
	public void switchFooterState(String newState) {
		fc.setCurrentMode(newState);
	}
	
	/**
	 * The default game state represents the state where the player can freely move about the grid.
	 * @return True if the user is in the default state, false otherwise.
	 */
	public boolean inDefaultState() {
		return (gc.selectedMode.equals("player") && sc.selectedMode.equals("free") && fc.selectedMode.equals("free"));
	}
}
