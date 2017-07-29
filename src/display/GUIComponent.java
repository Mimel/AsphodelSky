package display;

import java.awt.Dimension;

import javax.swing.JComponent;

/**
 * Abstract class that is a superclass of any GUI section in Asphodel Sky.
 * @author Matt Imel
 */
abstract class GUIComponent extends JComponent {

	/**
	 * The height of the component.
	 */
	protected int width;
	
	/**
	 * The width of the component.
	 */
	protected int height;
	
	/**
	 * X-Position of element relative to the upper-left corner.
	 */
	protected int xPos;
	
	/**
	 * Y-Position of element relative to the upper-left corner.
	 */
	protected int yPos;
	
	/**
	 * A list of all possible modes that a component can take.
	 * The default mode is always the first element in this list.
	 */
	protected String[] modes;
	
	/**
	 * The current mode.
	 */
	protected String selectedMode;
	
	GUIComponent(int x, int y, int w, int h) {
		this.xPos = x;
		this.yPos = y;
		this.width = w;
		this.height = h;
		
		this.setPreferredSize(new Dimension(w, h));
	}
	
	/**
	 * Sets the current mode of the component depending on the input parameter.
	 * If no mode with the string name is found, the default mode is used.
	 * @param newMode The mode to switch to, verbatim.
	 */
	void setCurrentMode(String newMode) {
		for (String mode : modes) {
			if (newMode.equals(mode)) {
				selectedMode = mode;
				return;
			}
		}
		
		selectedMode = modes[0];
	}
}
