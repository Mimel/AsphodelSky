package entity;

import display.SidebarComponent;
import grid.Grid;

/**
 * The player (You). 
 * @author Matt Imel
 */
public class Player extends Combatant {
	/**
	 * The output of the player.
	 */
	private SidebarComponent combatantOutput;
	
	public Player(String name, String title, int x, int y, int health, SidebarComponent sc) {
		super(0, name, title, health);
		
		this.setX(x);
		this.setY(y);
		this.combatantOutput = sc;
	}
	
	/**
	 * TODO: add description, move to Combatant.java.
	 */
	public void drawPlayer() {
		combatantOutput.drawCombatantInfo(this);
	}
}
