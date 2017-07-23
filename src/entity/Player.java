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
	
	public Player(String name, String title, int health, int science, SidebarComponent sc) {
		super(name, title, "", health, science);
		this.combatantOutput = sc;
	}
	
	/**
	 * TODO: add description, move to Combatant.java.
	 */
	public void updatePlayer() {
		combatantOutput.updateCombatantInfo(this);
	}

	/**
	 * The player should never act. This method returns, doing nothing.
	 * @param opai The operation to perform on this combatant.
	 */
	@Override
	public void act(OperationAI opai, int time, Grid gr) {
		return;
	}
}
