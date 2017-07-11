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

	/**
	 * The current display configuration.
	 */
	private DisplayConfiguration currentConfig;
	
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
}
