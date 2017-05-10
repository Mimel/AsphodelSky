package display;

import java.awt.Color;
import java.awt.Graphics;

/**
 * The uppermost component to the Asphodel Sky GUI; This typically contains information that needs to be
 * readily-known, such as a name, time, and floor.
 * @author Matt Imel
 */
public class GUIHeader extends GUIComponent implements HeaderComponent {
	
	public GUIHeader(int x, int y, int w, int h) {
		super(x, y, w, h);
	}
	
	/**
	 * Draws the header.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(0, 0, 200));
		g.fillRect(xPos, yPos, width, height);
	}

}
