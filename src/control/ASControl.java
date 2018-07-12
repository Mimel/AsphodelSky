package control;

import display.mainmenu.WindowController;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;

/**
 * The executing class.
 * @author Matt Imel
 */
public class ASControl {
	public static void main(String args[]) {
		// Forces JavaFX toolkit initialization.
		// Does nothing else.
		final JFXPanel fxp = new JFXPanel();

		new WindowController();
	}
}
