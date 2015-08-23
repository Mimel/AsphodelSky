package game;

import java.awt.Color;

public class FlavorText {
	private Color color;
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
