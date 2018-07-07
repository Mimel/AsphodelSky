package control;

import java.awt.Dimension;
import javax.swing.*;

import display.mainmenu.WindowController;

/**
 * The executing class.
 * @author Matt Imel
 */
public class ASControl {

	public static void main(String args[]) {
		JFrame gameWindow = new JFrame("Asphodel Sky");
		gameWindow.setMinimumSize(new Dimension(1200, 900));
		gameWindow.setMaximumSize(new Dimension(1920, 1080));
		gameWindow.setUndecorated(true);
		gameWindow.setLocationRelativeTo(null); //Centers window

		WindowController mc = new WindowController(gameWindow);
	}
}
