package entity;

import display.SidebarComponent;
import item.Catalog;

/**
 * The player (You). 
 * @author Matt Imel
 */
public class Player extends Combatant {
	
	/**
	 * The id of the player. This value should ALWAYS be zero.
	 * TODO: This assumes that there can be only one player.
	 */
	private final int id;
	
	/**
	 * The title of the player, denoting a brief description of the player's abilities.
	 */
	private String title;
	
	/**
	 * The name of the player.
	 */
	private String name;
	
	/**
	 * The X-coordinate of the player on the grid.
	 */
	private int xPosition;
	
	/**
	 * The Y-coordinate of the player on the grid.
	 */
	private int yPosition;
	
	/**
	 * The output of the player.
	 */
	private SidebarComponent combatantOutput;
	
	public Player(String name, String title, int x, int y, int health, SidebarComponent sc) {
		super(health);
		this.id = 0;
		this.name = name;
		this.title = title;
		
		this.xPosition = x;
		this.yPosition = y;

		this.combatantOutput = sc;
	}
	
	public int getId() { return id; }
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String n) {
		name = n;
	}
	
	public int getX() { return xPosition; }
	public int getY() { return yPosition; }
	
	public void setX(int newX) { xPosition = newX; }
	public void setY(int newY) { yPosition = newY; }
	
	/**
	 * TODO: add description, move to Combatant.java.
	 */
	public void drawPlayer() {
		combatantOutput.drawCombatantInfo(this);
	}
}
