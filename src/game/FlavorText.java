package game;

import java.awt.Color;

/**
 * A string combined with a color to provide a context for the message. Used only by the text feed in display.
 * @author Mimel
 */
public class FlavorText {
	/** The color of the text. */
	private Color color;
	
	/** The message the text reads. */
	private String text;
	
	public FlavorText(String t, char color) {
		this.text = t;
		switch(color) {
			case 'r':
				this.color = Color.red;
				break;
			case 'g':
				this.color = Color.green;
				break;
			case 'b':
				this.color = Color.blue;
				break;
			default:
				this.color = Color.black;
				break;
		}
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getText() {
		return text;
	}
	
	//Redundant?
	@Override
	public String toString() {
		return text;
	}
}
