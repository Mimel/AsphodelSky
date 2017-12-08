package entity;

import display.SidebarComponent;
import event.SimpleEvent;
import grid.CompositeGrid;

import java.util.List;

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
	public List<SimpleEvent> act(OperationAI opai, int time, CompositeGrid gr) {
		return null;
	}
}
