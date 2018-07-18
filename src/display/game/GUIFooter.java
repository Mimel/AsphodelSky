package display.game;

import dialogue.Statement;

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
	 * The horizontal banner, used to more easily differentiate the footer from its* higher peers.
	 */
	private Image tiledHorizontal;
	
	/**
	 * The overlay that displays the mode.
	 */
	private Image upperLeft;

	private GUIFooter_Switch contentSwitch;

	private FooterMode currentDisplayMode;

	private int x;

	private int y;

	private int width;

	private int height;

	public GUIFooter(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		
		currentDisplayMode = FooterMode.MESSAGES;
		
		//Initialize images.
		try {
			//Import image assets.
			tiledHorizontal = ImageIO.read(new File("img/footer/MSG_HorizontalEdge.png"));
			upperLeft = ImageIO.read(new File("img/footer/MSG_TopLeft.png"));
			
			//Import font assets.
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/kaushan-script/KaushanScript-Regular.otf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/sh-pinscher/SHPinscher-Regular.otf")));
			
		} catch (IOException | FontFormatException e) {
			e.printStackTrace();
		}

		this.contentSwitch = new GUIFooter_Switch();
	}

	public void insertItem(String name, String desc) {
		contentSwitch.updateDescription(name, desc);
	}

	public void insertDialogue(Statement root) {
		contentSwitch.loadDialogueTree(root);
	}

	public void shiftDialogueChoice(int adjustAddend) {
		contentSwitch.shiftChoice(adjustAddend);
	}

	public void progressDialogue() {
		contentSwitch.progressDialogueTree();
	}

	public boolean canDialogueContinue() {
		return !contentSwitch.isDialogueEnded();
	}
	
	/**
	 * Draws the footer.
	 */
	protected void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;	
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		//Background
		g2.setColor(new Color(200, 200, 200));
		g2.fillRect(0, 0, width, height);
		g2.drawImage(tiledHorizontal, 0, 0, null);
		g2.drawImage(upperLeft, 0, 0, null);
		
		//Content Title
		g2.setColor(new Color(255, 255, 255));
		g2.setFont(new Font("Kaushan Script", Font.PLAIN, 42));
		AffineTransform stdXY = g2.getTransform();
		g2.rotate(-Math.PI/32);
		
		if(currentDisplayMode.equals(FooterMode.MESSAGES)) {
			g2.drawString("Current Feed", 0, 70);
		} else if(currentDisplayMode.equals(FooterMode.DESCRIPTION)) {
			g2.drawString("Inventory", 0, 70);
		}
		
		g2.setTransform(stdXY);

		g2.setFont(new Font("SH Pinscher", Font.PLAIN, 22));
		contentSwitch.sendTo(g2, currentDisplayMode);
	}
}
