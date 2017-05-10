package display;

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
public class GUIFooter extends GUIComponent implements FooterComponent {
	
	/**
	 * The list of messages to write to the component.
	 */
	private String[] messages;
	
	/**
	 * When displaying items or people, the name of the item or person.
	 */
	private String source;
	
	/**
	 * When displaying items or people, the description of the item or the
	 * quotation of the person.
	 */
	private String description;
	
	/**
	 * The horizontal banner, used to more easily differentiate the footer from its
	 * higher peers.
	 */
	private Image tiledHorizontal;
	
	/**
	 * The overlay that displays the mode.
	 */
	private Image upperLeft;

	public GUIFooter(int x, int y, int w, int h) {
		super(x, y, w, h);
		
		this.messages = new String[1];
		
		this.source = "No source given.";
		
		this.description = "No description given.";
		
		//2 possible displays.
		modes = new String[]{"free", "descript"};
		
		//Starting mode displays messages.
		selectedMode = modes[0];
		
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
	}
	
	@Override
	public void drawMessages(String[] msgs) {
		this.messages = msgs;
		this.repaint();
	}
	
	public void insertItem(String name, String desc) {
		source = name;
		description = desc;
		
		this.repaint();
	}
	
	/**
	 * Draws the footer.
	 */
	@Override
	protected void paintComponent(Graphics g) {
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
		
		if(selectedMode == modes[0]) {
			g2.drawString("Current Feed", 0, 70);
		} else if(selectedMode == modes[1]) {
			g2.drawString("Inventory", 0, 70);
		}
		
		g2.setTransform(stdXY);
		
		//Draw message feed.
		if(selectedMode == modes[0]) {
			
			//Font
			g2.setColor(new Color(0, 0, 0));
			g2.setFont(new Font("SH Pinscher", Font.PLAIN, 22));
			
			//Messages
			for(int x = 0; x < messages.length; x++) {
				if(messages[x] != null) {
					g2.drawString(messages[x], 50, (-24 * x) + 215);
				}
			}
			
		} else if(selectedMode == modes[1]) { //Draws item description.
			
			//Font
			g2.setColor(new Color(0, 0, 0));
			g2.setFont(new Font("SH Pinscher", Font.PLAIN, 22));
			
			//Messages
			g2.drawString(source, 50, 95);
			g2.drawString(description, 50, 119);
			
		}
		
	}
	
}
