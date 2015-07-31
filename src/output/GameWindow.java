package output;

import java.awt.Dimension;
import javax.swing.JFrame;

import game.*;

public class GameWindow {
	public static void main(String[] args) {
		JFrame window = new JFrame("Asphodel Sky");
		window.setMinimumSize(new Dimension(1200, 900));
		window.setUndecorated(true);
		window.setLocationRelativeTo(null);
		Display d = new Display();
		window.add(d);
		
		window.setVisible(true);
	}
}
