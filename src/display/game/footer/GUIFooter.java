package display.game.footer;

import display.game.DrawingArea;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The bottom-most portion of the Asphodel Sky GUI, this typically contains descriptions or some sort
 * of text feed, depending on the mode of the component.
 * @author Matt Imel
 *
 */
public class GUIFooter {
	/**
	 * The horizontal banner, used to more easily differentiate the sidebar from its* higher peers.
	 */
	private Image tiledHorizontal;
	
	/**
	 * The overlay that displays the mode.
	 */
	private Image upperLeft;

	private final FooterContent messageState;
	private final FooterContent srcdescState;
	private final FooterContent dialogueState;
	private FooterContent currentState;

	private final DrawingArea bounds;

	public GUIFooter(int x, int y, int w, int h, FooterContent messageState, FooterContent srcdescState, FooterContent dialogueState) {
		this.bounds = new DrawingArea(x, y, w, h);

		this.messageState = messageState;
		this.srcdescState = srcdescState;
		this.dialogueState = dialogueState;
		this.currentState = this.messageState;
		
		//Initialize images.
		try {
			//Import image assets.
			tiledHorizontal = ImageIO.read(new File("img/sidebar/MSG_HorizontalEdge.png"));
			upperLeft = ImageIO.read(new File("img/sidebar/MSG_TopLeft.png"));
			
			//Import font assets.
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/kaushan-script/KaushanScript-Regular.otf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/sh-pinscher/SHPinscher-Regular.otf")));
			
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}
	}

	public void switchToMessages() {
		currentState = messageState;
	}

	public void switchToSrcDesc() {
		currentState = srcdescState;
	}

	public void switchToDialogue() {
		currentState = dialogueState;
	}
	
	/**
	 * Draws the sidebar.
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;	
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		//Background
		g2.setColor(new Color(200, 200, 200));
		g2.fillRect(bounds.getXOffset(), bounds.getYOffset(), bounds.getWidth(), bounds.getHeight());
		g2.drawImage(tiledHorizontal, bounds.getXOffset(), bounds.getYOffset(), null);
		g2.drawImage(upperLeft, bounds.getXOffset(), bounds.getYOffset(), null);
		
		//Content Title
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(new Font("Kaushan Script", Font.PLAIN, 42));
		AffineTransform stdXY = g2.getTransform();
		g2.rotate(-Math.PI/32);
		
		g2.setTransform(stdXY);

		g2.setFont(new Font("SH Pinscher", Font.PLAIN, 22));
		currentState.paintFooter(g, bounds);
	}
}
