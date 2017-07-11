package display;

import java.awt.Color;
import java.awt.Graphics;

/**
 * The uppermost component to the Asphodel Sky GUI; This typically contains information that needs to be
 * readily-known, such as a name, time, and floor.
 * @author Matt Imel
 */
public class GUIHeader extends GUIComponent implements HeaderComponent {

	private String title;

	private String time;
	
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

		if(title != null && time != null) {
			g.setColor(new Color(255, 255, 255));
			g.drawString(title, 30, 30);
			g.drawString(time, 200, 30);
		}
	}

	@Override
	public void setTitle(String newTitle) {
		this.title = newTitle;
	}

	@Override
	public void setTime(int newTime) {
		this.time = ((Integer)newTime).toString() + "s";
	}
}
